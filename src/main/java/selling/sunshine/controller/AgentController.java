package selling.sunshine.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import selling.sunshine.form.*;
import selling.sunshine.model.*;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.*;
import selling.sunshine.utils.*;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.*;

/**
 * 代理商的信息相关的所有请求入口
 * 参数列表中的code, state为通过微信Oauth2.0鉴权所携带的参数
 * Created by sunshine on 4/8/16.
 */
@RestController
@RequestMapping("/agent")
public class AgentController {
    private Logger logger = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private AgentService agentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BillService billService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private MessageService messageService;

    /**
     * 根据微信服务号的菜单入口传參,获取当前微信用户,并返回绑定账号页面
     *
     * @param code
     * @param state
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/bind")
    public ModelAndView bind(String code, String state) {
        ModelAndView view = new ModelAndView();
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state)) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "尊敬的代理商,请通过服务号内菜单入口进入绑定页面", "/agent/bind");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
        String openId = WechatUtil.queryOauthOpenId(code);
        view.addObject("wechat", openId);
        String url = "http://" + PlatformConfig.getValue("server_url") + "/agent/bind";
        String configUrl = url + "?code=" + code + "&state=" + state;
        try {
            String shareLink = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + PlatformConfig.getValue("wechat_appid") + "&redirect_uri=" + URLEncoder.encode(url, "utf-8") + "&response_type=code&scope=snsapi_base&state=view#wechat_redirect";
            Configuration configuration = WechatConfig.config(configUrl);
            configuration.setShareLink(shareLink);
            view.addObject("configuration", configuration);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        view.setViewName("/agent/wechat/bind");
        return view;
    }


    /**
     * 根据用户提交的绑定账号信息进行查询,并绑定微信号
     *
     * @param wechat
     * @param form
     * @param result
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/bind")
    public ModelAndView bind(String wechat, @Valid AgentLoginForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors() || StringUtils.isEmpty(wechat)) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "尊敬的代理商，请输入正确的账号", "/agent/bind");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("wechat", wechat);
        ResultData fetchResponse = agentService.fetchAgent(condition);
        if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_NULL) {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {
                subject.login(new UsernamePasswordToken(wechat, ""));
            }
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "尊敬的代理商，您的微信已经绑定过账号", "/agent/order/place");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
        Agent agent = new Agent(form.getPhone(), form.getPassword());
        ResultData loginResponse = agentService.login(agent);
        if (loginResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "尊敬的代理商，请输入正确的账号", "/agent/bind");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
        agent = (Agent) loginResponse.getData();
        agent.setWechat(wechat);
        ResultData modifyResponse = agentService.updateAgent(agent);
        if (modifyResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "绑定失败,请稍后重试", "/agent/bind");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
        if (!StringUtils.isEmpty(wechat)) {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {
                subject.login(new UsernamePasswordToken(wechat, ""));
            }
        }
        Prompt prompt = new Prompt(PromptCode.SUCCESS, "提示", "恭喜您,绑定成功!", "/agent/order/place");
        view.addObject("prompt", prompt);
        view.setViewName("/agent/prompt");
        return view;
    }

    /**
     * 获取密码找回的表单页面
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/lethe")
    public ModelAndView lethe() {
        ModelAndView view = new ModelAndView();
        String url = "http://" + PlatformConfig.getValue("server_url") + "/agent/lethe";
        String configUrl = url;
        try {
            String shareLink = url;
            Configuration configuration = WechatConfig.config(configUrl);
            configuration.setShareLink(shareLink);
            view.addObject("configuration", configuration);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        view.setViewName("/agent/lethe");
        return view;
    }

    /**
     * 代理商提交忘记密码表单,验证输入信息并帮代理商重置密码
     *
     * @param form
     * @param result
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/lethe")
    public ModelAndView lethe(@Valid AgentLetheForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            Prompt prompt = new Prompt(PromptCode.DANGER, "信息错误", "请您正确填写个人信息", "/agent/lethe");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("name", form.getName());
        condition.put("phone", form.getPhone());
        ResultData fetchResponse = agentService.fetchAgent(condition);
        if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "查不到对应信息的代理商", "/agent/lethe");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
        Agent agent = ((List<Agent>) fetchResponse.getData()).get(0);
        ResultData resetResponse = agentService.resetPassword(agent);
        if (resetResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Prompt prompt = new Prompt(PromptCode.SUCCESS, "提示", "密码重置成功,将通过短信发送到您的手机", "/agent/login");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        } else {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "重置操作出现问题,请重新尝试", "/agent/lethe");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
    }

    /**
     * 获取代理商登录页面
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public ModelAndView login(String warn) {
        ModelAndView view = new ModelAndView();
        String url = "http://" + PlatformConfig.getValue("server_url") + "/agent/login";
        String configUrl = url + "";
        Configuration configuration = WechatConfig.config(configUrl);
        configuration.setShareLink(url);
        view.addObject("configuration", configuration);
        view.setViewName("/agent/login");
        return view;
    }

    /**
     * 代理商通过用户名,密码登录提交表单
     *
     * @param form
     * @param result
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ModelAndView login(@Valid AgentLoginForm form, BindingResult result, RedirectAttributes attr) {
        ModelAndView view = new ModelAndView();
        //判断代理商填写的用户名和密码是否符合要求
        if (result.hasErrors()) {
            attr.addFlashAttribute("warn", "您的手机号或密码错误");
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
            attr.addFlashAttribute("warn", "您的手机号或密码错误");
            view.setViewName("redirect:/agent/login");
            return view;
        }
        //
        view.setViewName("redirect:/agent/order/place");
        return view;
    }

    /**
     * 获取代理商注册申请页面
     *
     * @param code
     * @param state
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/register")
    public ModelAndView register(String code, String state) {
        ModelAndView view = new ModelAndView();
        String url = "http://" + PlatformConfig.getValue("server_url") + "/agent/register";
        String configUrl;
        if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(code)) {
            String openId = WechatUtil.queryOauthOpenId(code);
            configUrl = url + "?code=" + code + "&state=" + state;
            view.addObject("wechat", openId);
        } else {
            configUrl = url + "";
        }
        try {
            String shareLink = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + PlatformConfig.getValue("wechat_appid") + "&redirect_uri=" + URLEncoder.encode(url, "utf-8") + "&response_type=code&scope=snsapi_base&state=view#wechat_redirect";
            Configuration configuration = WechatConfig.config(configUrl);
            configuration.setShareLink(shareLink);
            view.addObject("configuration", configuration);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        view.setViewName("/agent/register");
        return view;
    }

    /**
     * 代理商分享的推广链接
     *
     * @param agentId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{agentId}/embrace")
    public ModelAndView embrace(@PathVariable("agentId") String agentId) {
        ModelAndView view = new ModelAndView();
        if (!StringUtils.isEmpty(agentId)) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("agentId", agentId);
            condition.put("granted", true);
            ResultData fetchResponse = agentService.fetchAgent(condition);
            if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                Agent agent = ((List<Agent>) fetchResponse.getData()).get(0);
                view.addObject("upper", agent);
            }
        }
        String url = "http://" + PlatformConfig.getValue("server_url") + "/agent/" + agentId + "/embrace";
        String configUrl;
        configUrl = url + "";
        Configuration configuration = WechatConfig.config(configUrl);
        configuration.setShareLink(url);
        view.addObject("configuration", configuration);
        view.setViewName("/agent/register");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validate/{phone}")
    @ResponseBody
    public ResultData validate(@PathVariable String phone) {
        ResultData resultData = new ResultData();
        //验证有没有相同号码的用户注册过
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", phone);
        condition.put("blockFlag", false);
        condition.put("customerBlockFlag", false);
        resultData = agentService.fetchAgent(condition);
        return resultData;
    }

    /**
     * 代理商提交注册申请表单
     *
     * @param form
     * @param result
     * @param attr
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ModelAndView register(@Valid AgentForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        //验证表单信息是否符合限制要求
        if (result.hasErrors()) {
            view.setViewName("redirect:/agent/register");
            return view;
        }
        try {
            //验证有没有相同号码的用户注册过
            Map<String, Object> condition = new HashMap<>();
            condition.put("phone", form.getPhone());
            if (agentService.fetchAgent(condition).getResponseCode() == ResponseCode.RESPONSE_OK) {
                view.setViewName("redirect:/agent/register");
                return view;
            }
            //根据用户提交的表单构造代理信息
            Agent agent = new Agent(form.getName(), form.getGender(), form.getPhone(), form.getAddress(), form.getPassword(), form.getWechat(), Integer.parseInt(form.getMemberNum()));
            if (!StringUtils.isEmpty(form.getUpper())) {
                condition.clear();
                condition.put("agentId", form.getUpper());
                condition.put("blockFlag", false);
                ResultData agentResponse = agentService.fetchAgent(condition);
                if (agentResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    agent.setUpperAgent(new selling.sunshine.model.lite.Agent(((List<Agent>) agentResponse.getData()).get(0)));
                }
            }
            if (agent.getUpperAgent() == null) {
                condition.clear();
                condition.put("phone", form.getPhone());
                condition.put("customerBlockFlag", false);
                ResultData fetchResponse = customerService.fetchCustomer(condition);
                if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    Customer customer = ((List<Customer>) fetchResponse.getData()).get(0);
                    agent.setUpperAgent(customer.getAgent());
                }
            }
            ResultData createResponse = agentService.createAgent(agent);
            Credit credit = new Credit(form.getFront(), form.getBack(), new selling.sunshine.model.lite.Agent(agent));
            agentService.createCredit(credit);
            if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                Prompt prompt = new Prompt("提示", "您已成功提交申请,待审核通过后即可使用", "/agent/login");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
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

    /**
     * 获取代理商的个人信息菜单页面
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/me")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        //判断当前请求用户是否完成登录验证并具有相应的权限
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null || user.getAgent() == null) {
            view.setViewName("/agent/login");
            return view;
        }
        view.addObject("agent", user.getAgent());
        view.setViewName("/agent/me/index");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/place")
    public ModelAndView placeOrder(String code) {
        ModelAndView view = new ModelAndView();
        if (!StringUtils.isEmpty(code)) {
            oauth(code);
        }
        //获取当前登录的用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            if (!StringUtils.isEmpty(code)) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "尊敬的代理商，你尚未绑定账号！", "/agent/login");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            } else {
                view.setViewName("/agent/login");
                return view;
            }
        }
        //创建查询的删选条件集合
        Map<String, Object> condition = new HashMap<>();
        //查询商品信息
        condition.put("blockFlag", false);
        ResultData fetchGoodsResponse = commodityService.fetchCommodity(condition);
        if (fetchGoodsResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            view.addObject("goods", fetchGoodsResponse.getData());
        }
        //查询代理商的客户列表
        condition.clear();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("blockFlag", false);
        ResultData fetchCustomerResponse = customerService.fetchCustomer(condition);
        if (fetchCustomerResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            view.addObject("customer", fetchCustomerResponse.getData());
        }
        //查询平台当前配置的发货日期
        condition.clear();
        ResultData fetchShipmentResponse = shipmentService.fetchShipmentConfig(condition);
        if (fetchShipmentResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<ShipConfig> shipmentList = (List<ShipConfig>) fetchShipmentResponse.getData();
            if (shipmentList.isEmpty()) {
                //如果没有配置shipment
                view.addObject("hasConfig", false);
            } else {
                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                shipmentList.sort(new Comparator<ShipConfig>() {
                    @Override
                    public int compare(ShipConfig ship1, ShipConfig ship2) {
                        return Integer.valueOf(ship1.getDate()).compareTo(Integer.valueOf(ship2.getDate()));
                    }
                });
                int shipDay = 0;
                boolean isNextMonth = false;
                for (ShipConfig ship : shipmentList) {
                    if (ship.getDate() > currentDay) {
                        shipDay = ship.getDate();
                        break;
                    }
                }
                if (shipDay == 0) {
                    shipDay = shipmentList.get(0).getDate();
                    isNextMonth = true;
                }
                view.addObject("hasConfig", true);
                view.addObject("shipDay", shipDay);
                view.addObject("isNextMonth", isNextMonth);
            }
        }
        //根据代理商的ID查询代理商的详细信息
        condition.clear();
        condition.put("agentId", user.getAgent().getAgentId());
        ResultData queryAgentResponse = agentService.fetchAgent(condition);
        Agent agent = ((List<Agent>) queryAgentResponse.getData()).get(0);
        if (agent.isGranted()) {
            view.setViewName("/agent/order/place");
            return view;
        }
        Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "尊敬的代理商，您的资料现在正在审核中，只有当审核通过后才能代客下单，请耐心等待！", "/agent/login");
        view.addObject("prompt", prompt);
        view.setViewName("/agent/prompt");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/modify/{orderId}")
    public ModelAndView modifyOrder(@PathVariable("orderId") String orderId) {
        ModelAndView view = new ModelAndView();
        //获取当前登陆的用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            view.setViewName("/agent/login");
            return view;
        }
        Map<String, Object> condition = new HashMap<>();
        //获取商品列表
        condition.put("blockFlag", false);
        ResultData fetchGoodsResponse = commodityService.fetchCommodity(condition);
        if (fetchGoodsResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            view.addObject("goods", fetchGoodsResponse.getData());
        }
        //获取当前代理的客户列表
        condition.clear();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("blockFlag", false);
        ResultData fetchCustomerResponse = customerService.fetchCustomer(condition);
        if (fetchCustomerResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            view.addObject("customer", fetchCustomerResponse.getData());
        }
        //获取订单
        condition.clear();
        condition.put("orderId", orderId);
        ResultData fetchOrderResponse = orderService.fetchOrder(condition);
        if (fetchOrderResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Order order = ((List<Order>) fetchOrderResponse.getData()).get(0);
            view.addObject("order", order);
            view.addObject("status", order.getStatus());
        }
        view.addObject("operation", "MODIFY");
        //查询代理的详细信息
        condition.clear();
        condition.put("agentId", user.getAgent().getAgentId());
        ResultData fetchAgentResponse = agentService.fetchAgent(condition);
        Agent agent = ((List<Agent>) fetchAgentResponse.getData()).get(0);
        if (agent.isGranted()) {
            view.setViewName("/agent/order/modify");
            return view;
        }
        Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "尊敬的代理商，您的资料现在正在审核中，只有当审核通过后才能代客下单，请耐心等待！", "/agent/login");
        view.addObject("prompt", prompt);
        view.setViewName("/agent/prompt");
        return view;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/order/place/{type}")
    public ModelAndView placeOrder(@Valid OrderItemForm form, BindingResult result, RedirectAttributes attr, @PathVariable("type") String type) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/agent/order/place");
            return view;
        }
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            view.setViewName("/agent/login");
            return view;
        }
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        int length = form.getCustomerId().length;
        Order order = new Order();
        order.setAgent(user.getAgent());
        //创建订单和订单项
        double order_price = 0;
        for (int i = 0; i < length; i++) {
            String goodsId = form.getGoodsId()[i];//商品ID
            String customerId = form.getCustomerId()[i];//顾客ID
            String address = form.getAddress()[i];
            int goodsQuantity = Integer.parseInt(form.getGoodsQuantity()[i]);//商品数量
            double orderItemPrice = 0;//OrderItem总价
            Map<String, Object> goodsCondition = new HashMap<String, Object>();//查询商品价格
            goodsCondition.put("goodsId", goodsId);
            ResultData goodsData = commodityService.fetchCommodity(goodsCondition);
            Goods goods = null;
            if (goodsData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<Goods> goodsList = (List<Goods>) goodsData.getData();
                if (goodsList.size() != 1) {
                    Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "商品不唯一或未找到", "/agent/order/place");
                    attr.addFlashAttribute("prompt", prompt);
                    view.setViewName("redirect:/agent/prompt");
                    return view;
                }
                goods = goodsList.get(0);
            } else {
                Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "商品信息异常", "/agent/order/place");
                attr.addFlashAttribute("prompt", prompt);
                view.setViewName("redirect:/agent/prompt");
                return view;
            }
            orderItemPrice = goods.getPrice() * goodsQuantity;//得到一个OrderItem的总价
            order_price += orderItemPrice;//累加Order总价
            OrderItem orderItem = new OrderItem(customerId, goodsId, goodsQuantity, orderItemPrice, address);//构造OrderItem
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);//构造Order
        order.setPrice(order_price);
        switch (type) {
            case "save":
                order.setStatus(OrderStatus.SAVED);
                break;
            case "submit":
                order.setStatus(OrderStatus.SUBMITTED);
                break;
            default:
                order.setStatus(OrderStatus.SAVED);
        }

        ResultData fetchResponse = orderService.placeOrder(order);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (type.equals("save")) {
                Prompt prompt = new Prompt("提示", "保存成功", "/agent/order/manage/0");
                attr.addFlashAttribute("prompt", prompt);
                view.setViewName("redirect:/agent/prompt");
            } else if (type.equals("submit")) {
                view.setViewName("redirect:/order/pay/" + order.getOrderId());
            }
            return view;
        }
        Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "失败", "/agent/order/manage/0");
        attr.addFlashAttribute("prompt", prompt);
        view.setViewName("redirect:/agent/prompt");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/manage/{type}")
    public ModelAndView manageOrder(@PathVariable("type") String type) {
        ModelAndView view = new ModelAndView();
        view.addObject("type", type);
        view.setViewName("/agent/order/manage");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/order/list/{type}")
    public ResultData viewOrderList(@PathVariable("type") String type) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        ResultData result = new ResultData();
        if (user == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("您需要重新登录");
            return result;
        }
        List<SortRule> orderBy = new ArrayList<SortRule>();
        orderBy.add(new SortRule("create_time", "desc"));
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("status", type);
        condition.put("sort", orderBy);
        condition.put("blockFlag", false);
        ResultData fetchResponse = orderService.fetchOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(fetchResponse.getData());
        } else {
            result.setResponseCode(fetchResponse.getResponseCode());
            result.setDescription(fetchResponse.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/detail/{orderId}")
    public ModelAndView viewOrder(@PathVariable("orderId") String orderId) {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            view.setViewName("/agent/login");
            return view;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("orderId", orderId);
        ResultData fetchOrderResponse = orderService.fetchOrder(condition);
        Order order = ((List<Order>) fetchOrderResponse.getData()).get(0);
        view.addObject("order", order);
        view.addObject("status", order.getStatus());
        view.addObject("operation", "VIEW");
        view.setViewName("/agent/order/modify");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/customer/manage")
    public ModelAndView manageCustomer() {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            view.setViewName("/agent/login");
            return view;
        }
        //获取Agent详细信息
        Map<String, Object> condition = new HashMap<>();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("blockFlag", false);
        ResultData fetchResposne = agentService.fetchAgent(condition);
        if (fetchResposne.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Agent agent = ((List<Agent>) fetchResposne.getData()).get(0);
            view.addObject("agent", agent);
        }
        view.setViewName("/agent/customer/manage");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/customer/list")
    public ResultData viewCustomerList() {
        ResultData result = new ResultData();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("您需要重新登录");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("blockFlag", false);
        ResultData fetchResponse = customerService.fetchCustomer(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(fetchResponse.getData());
        } else {
            result.setResponseCode(fetchResponse.getResponseCode());
            result.setDescription(fetchResponse.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statement")
    public ModelAndView statement() {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            view.setViewName("/agent/login");
            return view;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", user.getAgent().getAgentId());
        ResultData fetchAgentResponse = agentService.fetchAgent(condition);
        if (fetchAgentResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return view;
        }
        Agent agent = ((List<Agent>) fetchAgentResponse.getData()).get(0);
        view.addObject("agent", agent);
        ResultData fetchRefundRecordResponse = refundService.fetchRefundRecord(condition);
        if (fetchRefundRecordResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return view;
        }
        List<RefundRecord> refundRecords = (List<RefundRecord>) fetchRefundRecordResponse.getData();
        view.addObject("refundRecords", refundRecords);
        view.setViewName("/agent/account/statement");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/refundConfig")
    public ModelAndView refundConfig() {
        ModelAndView view = new ModelAndView();
//        Map<String, Object> condition = new HashMap<String, Object>();
//        ResultData fetchRefundData = refundService.fetchRefundConfig(condition);
//        if (fetchRefundData.getResponseCode() != ResponseCode.RESPONSE_OK) {
//            return view;
//        }
//        List<RefundConfig> configs = (List<RefundConfig>) fetchRefundData.getData();

        view.setViewName("/agent/etc/refund_config");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/contact")
    public ModelAndView contact() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/etc/contact");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/modifypassword")
    public ModelAndView modifyPassword() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/etc/modify_password");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "modifypassword")
    public ModelAndView modifyPassword(@Valid PasswordForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors() || StringUtils.isEmpty(form.getPassword()) || !form.getPassword().equals(form.getPassword2())) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示信息", "尊敬的代理商，您输入的密码有误,请重新尝试", "/agent/modifypassword");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        Map<String, Object> condition = new HashMap<>();
        if (user != null) {
            selling.sunshine.model.lite.Agent agent = user.getAgent();
            condition.put("agentId", agent.getAgentId());
            condition.put("blockFlag", false);
            Agent target = ((List<Agent>) agentService.fetchAgent(condition).getData()).get(0);
            ResultData modiPassResponse = agentService.modifyPassword(target, form.getPassword());
            if (modiPassResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "提示信息", "修改密码失败,请重新尝试", "/agent/modifypassword");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            subject.logout();
            Prompt prompt = new Prompt(PromptCode.SUCCESS, "提示信息", "恭喜您,密码修改成功,请重新登录", "/agent/login");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        } else {
            view.setViewName("/agent/login");
            return view;
        }
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

    @RequestMapping(method = RequestMethod.GET, value = "/check/{agentId}")
    public ModelAndView check(@PathVariable String agentId) {

        ModelAndView view = new ModelAndView();
        if (StringUtils.isEmpty(agentId)) {
            view.setViewName("/backend/agent/check");
            return view;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", agentId);
        if (agentService.fetchAgent(condition).getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("/backend/agent/check");
            return view;
        }
        Agent agent = ((List<Agent>) agentService.fetchAgent(condition).getData()).get(0);

        if (agentService.fetchCredit(condition).getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("/backend/agent/grant");
            view.addObject("agent", agent);
            view.addObject("credit", "");
            return view;
        }
        Credit credit = ((List<Credit>) agentService.fetchCredit(condition).getData()).get(0);
        view.setViewName("/backend/agent/grant");
        view.addObject("agent", agent);
        view.addObject("credit", credit);
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
        condition.put("granted", false);
        ResultData fetchResponse = agentService.fetchAgent(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Agent>) fetchResponse.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/grant")
    public ModelAndView grant(String agentId) {
        ModelAndView view = new ModelAndView();
        if (StringUtils.isEmpty(agentId)) {
            view.setViewName("redirect:/agent/overview");
            return view;
        }
        Agent agent = new Agent();
        agent.setAgentId(agentId);
        agent.setGranted(true);
        ResultData updateResponse = agentService.updateAgent(agent);
        if (updateResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/agent/overview");
            return view;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", agent.getAgentId());
        condition.put("granted", true);
        ResultData fetchResponse = agentService.fetchAgent(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Agent targetAgent = ((List<Agent>) agentService.fetchAgent(condition).getData()).get(0);
            messageService.send(targetAgent.getPhone(), "尊敬的代理商" + targetAgent.getName() + ",您提交的申请信息已经审核通过,欢迎您的加入.【云草纲目】");
        }
        view.setViewName("redirect:/agent/overview");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/forbid/{agentId}")
    public ModelAndView forbid(@PathVariable("agentId") String agentId) {
        ModelAndView view = new ModelAndView();
        if (StringUtils.isEmpty(agentId)) {
            view.setViewName("redirect:/agent/overview");
            return view;
        }
        Agent agent = new Agent();
        agent.setAgentId(agentId);
        agent.setGranted(false);
        ResultData updateResponse = agentService.updateAgent(agent);
        if (updateResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/agent/overview");
            return view;
        }
        view.setViewName("redirect:/agent/overview");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/credit/{agentId}")
    public ModelAndView credit(@PathVariable("agentId") String agentId) {
        ModelAndView view = new ModelAndView();
        if (StringUtils.isEmpty(agentId)) {
            view.setViewName("redirect:/agent/overview");
            return view;
        }
        Agent agent = new Agent();
        agent.setAgentId(agentId);
        agent.setGranted(false);
        ResultData updateResponse = agentService.updateAgent(agent);
        if (updateResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/agent/overview");
            return view;
        }
        view.setViewName("redirect:/agent/overview");
        return view;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/agent/overview");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public DataTablePage<Agent> overview(DataTableParam param) {
        DataTablePage<Agent> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("granted", true);
        List<SortRule> rule = new ArrayList<>();
        rule.add(new SortRule("create_time", "desc"));
        condition.put("sort", rule);
        logger.debug(JSONObject.toJSONString(condition));
        ResultData fetchResponse = agentService.fetchAgent(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Agent>) fetchResponse.getData();
        }
        return result;
    }

    /**
     * 根据code做OAuth鉴权,并登录用户
     *
     * @param code
     */
    private void oauth(String code) {
        if (!StringUtils.isEmpty(code)) {
            try {
                String openId = WechatUtil.queryOauthOpenId(code);
                if (!StringUtils.isEmpty(openId)) {
                    Subject subject = SecurityUtils.getSubject();
                    if (!subject.isAuthenticated()) {
                        subject.login(new UsernamePasswordToken(openId, ""));
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
