package selling.sunshine.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.gift.ApplyForm;
import selling.sunshine.model.Admin;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.model.User;
import selling.sunshine.model.gift.GiftConfig;
import selling.sunshine.model.goods.Goods4Agent;
import selling.sunshine.model.lite.Agent;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.CommodityService;
import selling.sunshine.service.LogService;
import selling.sunshine.service.ToolService;
import selling.sunshine.utils.Prompt;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(method = RequestMethod.POST, value = "apply")
    public ModelAndView apply(@Valid ApplyForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("/agent/gift/apply");
            return view;
        }
        Prompt prompt = new Prompt("操作提示", "您的赠送申请已提交,请等待审批", "/agent/gift");
        view.addObject("prompt", prompt);
        view.setViewName("/agent/prompt");
        return view;
    }
}
