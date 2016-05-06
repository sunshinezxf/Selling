package selling.sunshine.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.AgentForm;
import selling.sunshine.form.AgentLoginForm;
import selling.sunshine.form.OrderItemForm;
import selling.sunshine.model.*;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.CommodityService;
import selling.sunshine.service.CustomerService;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
@RequestMapping("/agent")
@RestController
public class AgentController {
    private Logger logger = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private AgentService agentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET, value = "/me/index")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/me/index");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/place")
    public ModelAndView placeOrder() {
        ModelAndView view = new ModelAndView();

        Subject subject = SecurityUtils.getSubject();
        User user = null;
        if (subject != null) {
            Session session = subject.getSession();
            user = (User) session.getAttribute("current");
        }
        user = new User();
        Agent agent = new Agent();
        agent.setAgentId("王旻");
        user.setAgent(agent);
        Map<String, Object> condition = new HashMap<String, Object>();
        ResultData fetchDataGoods = commodityService.fetchCommodity(condition);
        ResultData fetchDataCustomers = customerService.fetchCustomer(user.getAgent());
        List<Goods> goods = null;
        List<Customer> customers = null;
        if (fetchDataGoods.getResponseCode() == ResponseCode.RESPONSE_OK) {
            goods = (List<Goods>) fetchDataGoods.getData();
        }
        if (fetchDataGoods.getResponseCode() == ResponseCode.RESPONSE_OK) {
            customers = (List<Customer>) fetchDataCustomers.getData();
        }
        logger.debug(goods == null ? "NULL!!!!!!!": "OK");
        logger.debug(customers == null ? "NULL!!!!!!!": "OK");
        view.addObject("goods", goods);
        view.addObject("customer", customers);
        view.setViewName("/agent/order/place");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/order/place")
    public ModelAndView placeOrder(@Valid OrderItemForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        int length = form.getCustomerId().length;
        Order order = new Order();
        Agent agent = new Agent();
        agent.setAgentId(form.getAgentId());
        order.setAgent(agent);
        for (int i = 0; i < length; i++) {
            OrderItem orderItem = new OrderItem(form.getCustomerId()[i], form.getGoodsId()[i], Integer.parseInt(form.getGoodsQuantity()[i]));
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        ResultData fetchResponse = orderService.placeOrder(order);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            view.setViewName("/agent/prompt");
        }
        view.setViewName("/agent/prompt");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/manage")
    public ModelAndView manageOrder() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/order/manage");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/customer/manage")
    public ModelAndView manageCustomer() {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User user = null;
        Agent agent=null;
        if (subject != null) {
            Session session = subject.getSession();
            user = (User) session.getAttribute("current");
            agent=user.getAgent();
        } 
        view.addObject("agent", agent);
        view.setViewName("/agent/customer/manage");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/account/info")
    public ModelAndView info() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/account/info");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statement")
    public ModelAndView statement() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/account/statement");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/register")
    public ModelAndView register() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/register");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ModelAndView register(@Valid AgentForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/agent/register");
            return view;
        }
        try {
            Agent agent = new Agent(form.getName(), form.getGender(), form.getPhone(), form.getAddress(), form.getPassword(), form.getWechat());
            ResultData createResponse = agentService.createAgent(agent);
            if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                view.setViewName("redirect:/agent/prompt");
                return view;
            } else {
                view.setViewName("redirect:/agent/register");
                return view;
            }
        } catch (Exception e) {
            view.setViewName("redirect:/agent/register");
            return view;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public ModelAndView login() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/login");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ModelAndView login(@Valid AgentLoginForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/agent/login");
            return view;
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated()) {
                view.setViewName("redirect:/agent/order/place");
                return view;
            }
            subject.login(new UsernamePasswordToken(form.getPhone(), form.getPassword()));
        } catch (Exception e) {
            view.setViewName("redirect:/agent/login");
            return view;
        }
        view.setViewName("redirect:/agent/order/place");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/prompt")
    public ModelAndView prompt() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/prompt");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/check")
    public ModelAndView check() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/agent/check");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/check")
    public DataTablePage<Agent> check(DataTableParam param) {
        DataTablePage<Agent> result = new DataTablePage<Agent>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("paid", false);
        ResultData fetchResponse = agentService.fetchAgent(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Agent>) fetchResponse.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/agent/overview");
        return view;
    }
}
