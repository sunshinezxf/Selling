package selling.sunshine.controller;

import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.user.User;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.AdminLoginForm;
import selling.sunshine.model.sum.OrderMonth;
import selling.sunshine.model.sum.TopThreeAgent;
import selling.sunshine.model.sum.Vendition;
import selling.sunshine.model.sum.VolumeTotal;
import selling.sunshine.service.*;
import selling.sunshine.vo.customer.CustomerVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

/**
 * 后台的平台接口
 * Created by sunshine on 4/10/16.
 */
@RequestMapping("/")
@RestController
public class PlatformController {

    private Logger logger = LoggerFactory.getLogger(PlatformController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LogService logService;

    @Autowired
    private StatisticService statisticService;

    /**
     * 跳转到后台登录界面
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        view.setViewName("redirect:/login");
        return view;
    }

    /**
     * 跳转到后台登录界面
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public ModelAndView login() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/login");
        return view;
    }

    /**
     * 登录
     * @param form
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ModelAndView login(@Valid AdminLoginForm form, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/login");
            return view;
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated() && subject.getPrincipal() != null) {
                subject.logout();
            }
            subject.login(new UsernamePasswordToken(form.getUsername(), form.getPassword()));
            User user = (User) subject.getPrincipal();
            HttpSession session = request.getSession();
            session.setAttribute("role", user.getRole().getName());
            if (!StringUtils.isEmpty(user.getAgent())) {
                view.setViewName("redirect:/me");
            } else {
                view.setViewName("redirect:/dashboard");
            }
        } catch (Exception e) {
            view.setViewName("redirect:/login");
            return view;
        }
        return view;
    }

    /**
     * 跳转到后台的首页
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/dashboard")
    public ModelAndView dashboard() {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        ResultData resultData = new ResultData();

        resultData = statisticService.orderMonth();
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            OrderMonth orderMonth = ((List<OrderMonth>) resultData.getData()).get(0);
            orderMonth.setPrice(((int) (orderMonth.getPrice() * 100) * 1.0 / 100));
            orderMonth.setTotalPrice(((int) (orderMonth.getTotalPrice() * 100) * 1.0 / 100));
            view.addObject("orderMonth", orderMonth);
        } else {
            OrderMonth orderMonth = new OrderMonth();
            view.addObject("orderMonth", orderMonth);
        }

        condition.put("monthly", true);
        condition.put("type", 0);
        resultData = statisticService.purchaseRecord(condition);
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Vendition> monthlyGoods = (List<Vendition>) resultData.getData();
            for (int i = 0; i < monthlyGoods.size(); i++) {
                monthlyGoods.get(i).setRecordPrice(((int) (monthlyGoods.get(i).getRecordPrice() * 100) * 1.0 / 100));
            }
            view.addObject("monthlyGoods", monthlyGoods);
        } else {
            //无记录
        }

        condition.clear();
        condition.put("type", 0);
        resultData = statisticService.purchaseRecord(condition);
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Vendition> totalGoods = (List<Vendition>) resultData.getData();
            for (int i = 0; i < totalGoods.size(); i++) {
                totalGoods.get(i).setRecordPrice(((int) (totalGoods.get(i).getRecordPrice() * 100) * 1.0 / 100));
            }
            view.addObject("totalGoods", totalGoods);
        } else {
            //无记录
        }

        condition.clear();
        condition.put("monthly", true);
        condition.put("type", 0);
        List<Integer> status = new ArrayList<Integer>();
        status.add(1);
        condition.put("status", status);
        resultData = statisticService.purchaseRecord(condition);
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Vendition> payedRecord = (List<Vendition>) resultData.getData();
            view.addObject("payedRecord", payedRecord);
        } else {
            //无记录
            logger.debug("lalalalallalalala");
        }

        condition.clear();
        condition.put("monthly", true);
        condition.put("type", 0);
        status.clear();
        status.add(2);
        status.add(3);
        condition.put("status", status);
        resultData = statisticService.purchaseRecord(condition);
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Vendition> shippedRecord = (List<Vendition>) resultData.getData();
            view.addObject("shippedRecord", shippedRecord);
        } else {
            //无记录
        }

        condition.clear();
        condition.put("monthly", true);
        condition.put("type", 1);
        resultData = statisticService.purchaseRecord(condition);
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Vendition> giftRecord = (List<Vendition>) resultData.getData();
            view.addObject("giftRecord", giftRecord);
        } else {
            //无记录
        }

        resultData = statisticService.topThreeAgent();
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Object> list = (List<Object>) resultData.getData();
            List<TopThreeAgent> monthList = (List<TopThreeAgent>) list.get(0);
            List<TopThreeAgent> allList = (List<TopThreeAgent>) list.get(1);
            if (monthList.size() != 0) {
                view.addObject("monthList", monthList);
            }
            if (allList.size() != 0) {
                view.addObject("allList", allList);
            }
        }
        view.setViewName("/backend/dashboard");
        return view;
    }

    /**
     * 退出后台
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    public ModelAndView logout() {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        view.setViewName("redirect:/login");
        return view;
    }

    /**
     * 导航栏
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/navigate")
    public ModelAndView navigate() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/navigate");
        return view;
    }

    /**
     * 日志
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/log")
    public ModelAndView log() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/system/log");
        return view;
    }

    /**
     * 代理商登录后台后，跳转到代理商个人中心界面
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/me")
    public ModelAndView me() {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (StringUtils.isEmpty(user.getAgent())) {
            subject.logout();
            view.setViewName("redirect:/login");
            return view;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("agentId", user.getAgent().getAgentId());
        ResultData responose = agentService.fetchAgent(condition);
        if (responose.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Agent agent = ((List<Agent>) responose.getData()).get(0);
            view.addObject("agent", agent);
        }
        // 代理商列表（除该代理商之外的其他已授权的代理商列表，用来配置该代理商的上级代理商）
        condition.clear();
        condition.put("granted", true);
        condition.put("blockFlag", false);
        condition.put("sortByName", true);
        condition.put("agentType", 0);//只查询普通代理商
        responose = agentService.fetchAgent(condition);
        if (responose.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Agent> agentList = (List<Agent>) responose.getData();
            Iterator<Agent> iter = agentList.iterator();
            while (iter.hasNext()) {
                if (iter.next().getAgentId().equals(user.getAgent().getAgentId())) {
                    iter.remove();
                }
            }
            view.addObject("agentList", agentList);
        }

        // 顾客人数
        condition.clear();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("blockFlag", false);
        responose = customerService.fetchCustomer(condition);
        if (responose.getResponseCode() == ResponseCode.RESPONSE_OK) {
            int customerNum = ((List<CustomerVo>) responose.getData()).size();
            view.addObject("customerNum", customerNum);
        } else {
            view.addObject("customerNum", 0);
        }
        // 代理商统计信息
        // 累计销售信息
        responose = statisticService.queryAgentGoods(condition);
        if (responose.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<VolumeTotal> volumeTotalList = (List<VolumeTotal>) responose.getData();
            view.addObject("volumeTotalList", volumeTotalList);
        }

        // 排名
        condition.clear();
        condition.put("blockFlag", false);
        condition.put("granted", true);
        condition.put("agentType", 0);//只查询普通代理商
        responose = agentService.fetchAgent(condition);
        if (responose.getResponseCode() == ResponseCode.RESPONSE_OK) {
            int totalNum = ((List<Agent>) responose.getData()).size();
            view.addObject("totalNum", totalNum);
        }
        condition.clear();
        condition.put("agentId", user.getAgent().getAgentId());
        responose = statisticService.agentRanking(condition);
        if (responose.getResponseCode() == ResponseCode.RESPONSE_OK) {
            int ranking = (int) responose.getData();
            view.addObject("ranking", ranking);
        }

        view.setViewName("/backend/agent/personalcenter");
        return view;
    }

}
