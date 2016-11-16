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
import selling.sunshine.form.gift.ApplyConfigForm;
import selling.sunshine.form.gift.ApplyForm;
import selling.sunshine.form.gift.ConfigForm;
import common.sunshine.model.selling.admin.Admin;
import selling.sunshine.model.BackOperationLog;
import common.sunshine.model.selling.user.User;
import selling.sunshine.model.gift.GiftApply;
import selling.sunshine.model.gift.support.GiftApplyStatus;
import selling.sunshine.model.gift.GiftConfig;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.model.selling.agent.lite.Agent;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.CommodityService;
import selling.sunshine.service.LogService;
import selling.sunshine.service.ToolService;
import selling.sunshine.utils.Prompt;
import selling.sunshine.utils.PromptCode;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import common.sunshine.utils.SortRule;

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
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("agentId", agentId);
        condition.put("goodsId", goodsId);
        ResultData response = agentService.fetchAgentGift(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/config")
    public ModelAndView giftConfig(@Valid ConfigForm form, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/gift/check");
            return view;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("agentId", form.getAgentId());
        condition.put("goodsId", form.getGoodsId());
        ResultData response = agentService.fetchAgentGift(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            Agent agent = new Agent();
            agent.setAgentId(form.getAgentId());
            Goods4Agent goods = new Goods4Agent();
            goods.setGoodsId(form.getGoodsId());
            GiftConfig giftConfig = new GiftConfig(agent, goods, Integer.parseInt(form.getAmount()));
            response = agentService.createAgentGift(giftConfig);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                view.setViewName("redirect:/gift/check");
                return view;
            }
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            GiftConfig giftConfig = ((List<GiftConfig>) response.getData()).get(0);
            giftConfig.setAmount(Integer.parseInt(form.getAmount()));
            agentService.updateAgentGift(giftConfig);
        }
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null || user.getAdmin() == null) {
            view.setViewName("redirect:/login");
            return view;
        }
        Admin admin = user.getAdmin();
        condition.clear();
        condition.put("agentId", form.getAgentId());
        common.sunshine.model.selling.agent.Agent agent = ((List<common.sunshine.model.selling.agent.Agent>) agentService.fetchAgent(condition).getData()).get(0);
        BackOperationLog backOperationLog = new BackOperationLog(
                admin.getUsername(), toolService.getIP(request), "管理员" + admin.getUsername() + "修改了代理商" + agent.getName() + "的赠送配置");
        logService.createbackOperationLog(backOperationLog);
        view.setViewName("redirect:/gift/check");
        return view;
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
        
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.clear();
        condition.put("agentId", user.getAgent().getAgentId());
        List<SortRule> orderBy = new ArrayList<>();
        orderBy.add(new SortRule("create_time", "desc"));
        condition.put("sort", orderBy);
        ResultData fetchApplyResponse = agentService.fetchGiftApply(condition);
        if(fetchApplyResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
        	GiftApply apply = ((List<GiftApply>)fetchApplyResponse.getData()).get(0);
        	if(apply.getStatus() == GiftApplyStatus.APPLYED){
        		Prompt prompt = new Prompt(PromptCode.DANGER, "操作提示", "您有审核中的赠送申请，请先等待审核", "/agent/gift");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
        	}
        }
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

    @RequestMapping(method = RequestMethod.POST, value = "/apply/handle")
    public ModelAndView handleApply(@Valid ApplyConfigForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/gift/check");
            return view;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("applyId", form.getApplyId());
        ResultData response = agentService.fetchGiftApply(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/gift/check");
            return view;
        }
        GiftApply apply = ((List<GiftApply>) response.getData()).get(0);
        condition.clear();
        condition.put("agentId", apply.getAgent().getAgentId());
        condition.put("goodsId", apply.getGoods().getGoodsId());
        response = agentService.fetchAgentGift(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            GiftConfig config = ((List<GiftConfig>) response.getData()).get(0);
            config.setAmount(Integer.parseInt(form.getAmount()));
            agentService.updateAgentGift(config);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            Agent agent = new Agent();
            agent.setAgentId(apply.getAgent().getAgentId());
            Goods4Agent goods = new Goods4Agent();
            goods.setGoodsId(apply.getGoods().getGoodsId());
            GiftConfig config = new GiftConfig(agent, goods, Integer.parseInt(form.getAmount()));
            agentService.createAgentGift(config);
        }
        apply.setStatus(GiftApplyStatus.PROCESSED);
        agentService.updateGiftApply(apply);
        view.setViewName("redirect:/gift/check");
        return view;
    }
}
