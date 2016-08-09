package selling.sunshine.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.gift.ApplyForm;
import selling.sunshine.model.Admin;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.model.User;
import selling.sunshine.model.gift.GiftApply;
import selling.sunshine.model.gift.GiftApplyStatus;
import selling.sunshine.model.gift.GiftConfig;
import selling.sunshine.model.goods.Goods4Agent;
import selling.sunshine.model.lite.Agent;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.CommodityService;
import selling.sunshine.service.LogService;
import selling.sunshine.service.ToolService;
import selling.sunshine.utils.Prompt;
import selling.sunshine.utils.PromptCode;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/gift")
public class GiftController {

    private Logger logger = LoggerFactory.getLogger(GiftController.class);

    @Autowired
    private AgentService agentService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private LogService logService;

    @RequestMapping(method = RequestMethod.GET, value = "/{agentId}/{goodsId}")
    public ResultData agentGift(@PathVariable("agentId") String agentId, @PathVariable("goodsId") String goodsId) {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", agentId);
        condition.put("goodsId", goodsId);
        resultData = agentService.fetchAgentGift(condition);
        return resultData;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/config/{agentId}/{goodsId}/{amount}")
    public ResultData giftConfig(@PathVariable("agentId") String agentId, @PathVariable("goodsId") String goodsId, @PathVariable("amount") int amount, HttpServletRequest request) {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", agentId);
        condition.put("goodsId", goodsId);
        ResultData queryData = agentService.fetchAgentGift(condition);
        if (queryData.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            Agent agent = new Agent();
            agent.setAgentId(agentId);
            Goods4Agent goods = new Goods4Agent();
            goods.setGoodsId(goodsId);
            GiftConfig giftConfig = new GiftConfig(agent, goods, amount);
            resultData = agentService.createAgentGift(giftConfig);
        } else {
            GiftConfig giftConfig = ((List<GiftConfig>) queryData.getData()).get(0);
            giftConfig.setAmount(amount);
            resultData = agentService.updateAgentGift(giftConfig);
        }
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return resultData;
        }
        Admin admin = user.getAdmin();
        condition.clear();
        condition.put("agentId", agentId);
        selling.sunshine.model.Agent agent = ((List<selling.sunshine.model.Agent>) agentService.fetchAgent(condition).getData()).get(0);
        BackOperationLog backOperationLog = new BackOperationLog(
                admin.getUsername(), toolService.getIP(request), "管理员" + admin.getUsername() + "修改了代理商" + agent.getName() + "的赠送配置");
        logService.createbackOperationLog(backOperationLog);

        return resultData;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/apply")
    public ModelAndView apply() {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = commodityService.fetchGoods4Agent(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Goods4Agent> list = (List<Goods4Agent>) response.getData();
            view.addObject("goods", list);
        }
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user != null && user.getAgent() != null) {
            view.addObject("name", user.getAgent().getName());
        } else {
            view.setViewName("redirect:/agent/login");
            return view;
        }
        view.setViewName("/agent/gift/apply");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/apply")
    public ModelAndView apply(@Valid ApplyForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("/agent/gift/apply");
            return view;
        }
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        Agent agent = user.getAgent();
        Goods4Agent goods = new Goods4Agent();
        goods.setGoodsId(form.getGoodsId());
        GiftApply apply = new GiftApply(Integer.parseInt(form.getPotential()), Integer.parseInt(form.getApplyLine()), agent, goods, Integer.parseInt(form.getLastQuantity()), Integer.parseInt(form.getTotalQuantity()));
        ResultData response = agentService.createGiftApply(apply);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            Prompt prompt = new Prompt(PromptCode.DANGER, "操作提示", "您的赠送申请提交失败", "/agent/gift");
            view.addObject("prompt", prompt);
        } else {
            Prompt prompt = new Prompt("操作提示", "您的赠送申请已提交,请等待审批", "/agent/gift");
            view.addObject("prompt", prompt);
        }
        view.setViewName("/agent/prompt");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/check")
    public ModelAndView check() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/agent/gift/check");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/check")
    public DataTablePage<GiftApply> check(DataTableParam param) {
        DataTablePage<GiftApply> result = new DataTablePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>(Arrays.asList(0));
        condition.put("status", status);
        condition.put("blockFlag", false);
        ResultData fetchResponse = agentService.fetchGiftApply(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<GiftApply>) fetchResponse.getData();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/apply/{applyId}")
    public ResultData view(@PathVariable("applyId") String applyId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("applyId", applyId);
        ResultData response = agentService.fetchGiftApply(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(((List<GiftApply>) response.getData()).get(0));
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/apply/decline")
    public ResultData decline(String applyId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("applyId", applyId);
        ResultData response = agentService.fetchGiftApply(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            response.setDescription(response.getDescription());
        }
        GiftApply apply = ((List<GiftApply>) response.getData()).get(0);
        if (apply.getStatus() != GiftApplyStatus.APPLYED) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("该申请已经被处理");
            return result;
        }
        apply.setStatus(GiftApplyStatus.REJECTED);
        response = agentService.declineGiftApply(apply);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setDescription(response.getDescription());
        }
        return result;
    }
}
