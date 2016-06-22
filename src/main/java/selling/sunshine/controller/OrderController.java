package selling.sunshine.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Charge;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import selling.sunshine.form.ExpressForm;
import selling.sunshine.form.OrderItemForm;
import selling.sunshine.form.PayForm;
import selling.sunshine.form.SortRule;
import selling.sunshine.model.*;
import selling.sunshine.model.goods.Goods4Agent;
import selling.sunshine.pagination.MobilePage;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.service.*;
import selling.sunshine.utils.Prompt;
import selling.sunshine.utils.PromptCode;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import selling.sunshine.utils.WechatConfig;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/11/16.
 */
@RequestMapping("/order")
@RestController
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private BillService billService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private RefundService refundService;

    @RequestMapping(method = RequestMethod.GET, value = "/check")
    public ModelAndView handle() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/check");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/check")
    public MobilePage<Order> handle(MobilePageParam param) {
        MobilePage<Order> result = new MobilePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(2);
        condition.put("status", status);
        List<SortRule> rule = new ArrayList<>();
        rule.add(new SortRule("create_time", "asc"));
        condition.put("sort", rule);
        ResultData fetchResponse = orderService.fetchOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (MobilePage<Order>) fetchResponse.getData();
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/customerOrder/check")
    public MobilePage<CustomerOrder> customerOrderHandle(MobilePageParam param) {
        MobilePage<CustomerOrder> result = new MobilePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(1);
        condition.put("status", status);
        List<SortRule> rule = new ArrayList<>();
        rule.add(new SortRule("create_time", "asc"));
        condition.put("sort", rule);
        ResultData fetchResponse = orderService.fetchCustomerOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (MobilePage<CustomerOrder>) fetchResponse.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/overview");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public MobilePage<Order> overview(MobilePageParam param) {
        MobilePage<Order> result = new MobilePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        switch (Integer.parseInt((String) param.getParams().get("status"))) {
            case 0:
                status.add(0);
                break;
            case 1:
                status.add(1);
                break;
            case 2:
                status.add(2);
                status.add(3);
                status.add(4);
                break;
            case 3:
                status.add(5);
                break;
        }
        condition.put("status", status);
        ResultData fetchResponse = orderService.fetchOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (MobilePage<Order>) fetchResponse.getData();
        }
        return result;
    }    
    
    @RequestMapping(method = RequestMethod.GET, value = "/customerOrder/overview")
    public ModelAndView customerOrderOverview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/customerOrder");
        return view;
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/customerOrder/overview")
    public MobilePage<CustomerOrder> customerOrderOverview(MobilePageParam param) {
        MobilePage<CustomerOrder> result = new MobilePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        switch (Integer.parseInt((String) param.getParams().get("status"))) {
            case 0:
                status.add(0);
                break;
            case 1:
                status.add(1);
                break;
            case 2:
                status.add(2);
                break;
            case 3:
                status.add(3);
                break;
        }
        condition.put("status", status);
        ResultData fetchResponse = orderService.fetchCustomerOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (MobilePage<CustomerOrder>) fetchResponse.getData();
        }
        return result;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/express")
    public ModelAndView express() {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(2);
        condition.put("status", status);
        List<Order> orderList = (List<Order>) orderService
                .fetchOrder(condition).getData();
        List<Express> expressList = new ArrayList<>();
        Timestamp expressDate = new Timestamp(System.currentTimeMillis());
        int k=0;
        for (int j = 0; j < orderList.size(); j++) {
            Order order = orderList.get(j);
            // 验证order的每一项orderItem购买的商品的数量与相应的价格是否一致
            // 若不一致，则将不一致的那一项删除，并且把钱退回给代理商并告知他
            // 同时，根据不同情况修改order的状态和orderItem的状态
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem item : orderItems) {
                if (item.getOrderItemPrice() != (item.getGoodsQuantity() * item
                        .getGoods().getAgentPrice())) {

                }
            }
            
            for (int i = 0; i < orderItems.size(); i++) {
                Express express = new Express("待填", "云草纲目", "18000000000",
                        "云南", orderItems.get(i).getCustomer().getName(),
                        orderItems.get(i).getCustomer().getPhone().getPhone(),
                        orderItems.get(i).getCustomer().getAddress()
                                .getAddress(), orderItems.get(i).getGoods()
                                .getName(), expressDate);
                express.setExpressId("expressNumber" +k);
                k++;
                express.setOrderItem(orderItems.get(i));
                expressList.add(express);
            }
        }
        
        //添加顾客订单
        status.clear();
        condition.clear();
        status.add(1);
        condition.put("status", status);
        List<CustomerOrder> customerOrderList=(List<CustomerOrder>)orderService.fetchCustomerOrder(condition).getData();
        for (CustomerOrder customerOrder:customerOrderList) {
        	 Express express = new Express("待填", "云草纲目", "18000000000",
                     "云南", customerOrder.getReceiverName(),
                     customerOrder.getReceiverPhone(),
                    customerOrder.getReceiverAddress(), customerOrder.getGoods().getName(), expressDate);
             express.setExpressId("expressNumber" +k);
             k++;
            // express.setOrderItem(orderItems.get(i));
             expressList.add(express);
		}
        view.addObject("expressList", expressList);
        view.setViewName("/backend/order/express");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/express")
    public ModelAndView express(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        if ((request.getParameter("orderId") == null)&&(request.getParameter("customerOrderId") == null)) {
            view.setViewName("/backend/order/express");
            return view;
        }
        Timestamp expressDate = new Timestamp(System.currentTimeMillis());
        List<Express> expressList = new ArrayList<>();
        if (request.getParameter("orderId") != null) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("orderId", request.getParameter("orderId"));
            ResultData orderData = orderService.fetchOrder(condition);
            Order order = ((List<Order>) orderData.getData()).get(0);
            // 验证order的每一项orderItem购买的商品的数量与相应的价格是否一致
            // 若不一致，则将不一致的那一项删除，并且把钱退回给代理商并告知他
            // 同时，根据不同情况修改order的状态和orderItem的状态
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem item : orderItems) {
                if (item.getOrderItemPrice() != (item.getGoodsQuantity() * item.getGoods().getAgentPrice())) {

                }
            }
            for (int i = 0; i < orderItems.size(); i++) {
                Express express = new Express("待填", "云草纲目", "18000000000", "云南", orderItems.get(i).getCustomer().getName(), orderItems.get(i).getCustomer().getPhone().getPhone(), orderItems.get(i).getCustomer().getAddress().getAddress(), orderItems.get(i).getGoods().getName(), expressDate);
                express.setExpressId("expressNumber" + i);
                express.setOrderItem(orderItems.get(i));
                expressList.add(express);
            }
		}else {
			 Map<String, Object> condition = new HashMap<>();
	         condition.put("orderId", request.getParameter("customerOrderId"));
	         CustomerOrder customerOrder=((List<CustomerOrder>)orderService.fetchCustomerOrder(condition).getData()).get(0);
	         Express express = new Express("待填", "云草纲目", "18000000000",
                     "云南", customerOrder.getReceiverName(),
                     customerOrder.getReceiverPhone(),
                    customerOrder.getReceiverAddress(), customerOrder.getGoods().getName(), expressDate);
             express.setExpressId("expressNumber" +0);
             expressList.add(express);
		}

        view.addObject("expressList", expressList);
        view.setViewName("/backend/order/express");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/expressData")
    @ResponseBody
    public ResultData express(@RequestBody List<ExpressForm> expressData) {
        ResultData resultData = new ResultData();
        List<Express> expressList = expressData.get(0).getExpressList();
        String expressNumber = expressData.get(0).getExpressNumber();
        Long num = Long.parseLong(expressNumber);
        for (Express express : expressList) {
            express.setExpressNumber(String.valueOf(num));
            num++;
            expressService.createExpress(express);
            if (express.getOrderItem() != null) {
                express.getOrderItem().setStatus(OrderItemStatus.SHIPPED);
                orderService.updateOrderItem(express.getOrderItem());
                Map<String, Object> condition = new HashMap<>();
                condition.put("orderItemId", express.getOrderItem().getOrderItemId());
                OrderItem item = ((List<OrderItem>) orderService.fetchOrderItem(condition).getData()).get(0);
                if (item.getOrder().getStatus() != OrderStatus.FULLY_SHIPMENT) {
                    item.getOrder().setStatus(OrderStatus.FULLY_SHIPMENT);
                    orderService.modifyOrder(item.getOrder());
                }
            }
        }
        resultData.setResponseCode(ResponseCode.RESPONSE_OK);
        return resultData;

    }

    @RequestMapping(method = RequestMethod.POST, value = "/orderItem/{orderId}")
    @ResponseBody
    public ResultData overviewOrderItem(
            @PathVariable("orderId") String orderId) {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        ResultData orderData = orderService.fetchOrder(condition);
        Order order = ((List<Order>) orderData.getData()).get(0);
        double totalPrices = order.getPrice();
        Map<String, Object> goods_quantity_Map = new HashMap<>();
        for (OrderItem item : order.getOrderItems()) {
            String goodsName = item.getGoods().getName();
            int goodsQuantity = item.getGoodsQuantity();
            if (goods_quantity_Map.containsKey(goodsName)) {
                goods_quantity_Map
                        .put(goodsName,
                                ((int) goods_quantity_Map.get(goodsName) + goodsQuantity));
            } else {
                goods_quantity_Map.put(goodsName, goodsQuantity);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("totalPrices", totalPrices);
        result.put("goods_quantity_Map", goods_quantity_Map);
        resultData.setData(result);
        return resultData;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/statistics")
    @ResponseBody
    public ResultData statistics() {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(2);
        condition.put("status", status);
        List<Order> orderList = (List<Order>) orderService.fetchOrder(condition).getData();
        
        
        condition.clear();
        status.clear();
        status.add(1);
        condition.put("status", status);
        List<CustomerOrder> customerOrderList=(List<CustomerOrder>)orderService.fetchCustomerOrder(condition).getData();
        
        
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("orderList", orderList);
        dataMap.put("customerOrderList", customerOrderList);
        resultData.setData(dataMap);

        return resultData;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/save")
    public ModelAndView save() {
        ModelAndView view = new ModelAndView();
        view.setViewName("");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cancel/{orderId}")
    public ModelAndView cancelOrder(@PathVariable("orderId") String orderId,
                                    RedirectAttributes attr) {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
        	WechatConfig.oauthWechat(view, "/agent/login");
            view.setViewName("/agent/login");
            return view;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("orderId", orderId);
        ResultData orderFetchData = orderService.fetchOrder(condition);
        if (orderFetchData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "取消失败",
                    "/agent/order/manage/0");
            attr.addFlashAttribute("prompt", prompt);
            view.setViewName("redirect:/agent/prompt");
            return view;
        }
        Order order = ((List<Order>) orderFetchData.getData()).get(0);
        order.setBlockFlag(true);
        ResultData modifyFetchData = orderService.cancel(order);
        if (modifyFetchData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "取消失败",
                    "/agent/order/manage/0");
            attr.addFlashAttribute("prompt", prompt);
            view.setViewName("redirect:/agent/prompt");
            return view;
        }
        Prompt prompt = new Prompt("提示", "成功", "/agent/order/manage/0");
        attr.addFlashAttribute("prompt", prompt);
        view.setViewName("redirect:/agent/prompt");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/modify/{type}")
    public ModelAndView modifyOrder(@Valid OrderItemForm form,
                                    BindingResult result, RedirectAttributes attr,
                                    @PathVariable("type") String type, String openId) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/order/list/0");
            return view;
        }
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
        	WechatConfig.oauthWechat(view, "/agent/login");
            view.setViewName("/agent/login");
            return view;
        }
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        int length = form.getCustomerId().length;
        Order order = new Order();
        order.setAgent(user.getAgent());
        // 构造订单和订单项
        double total_price = 0;
        for (int i = 0; i < length; i++) {
            String goodsId = form.getGoodsId()[i];// 商品ID
            String customerId = form.getCustomerId()[i];// 顾客ID
            String orderItemId = form.getOrderItemId()[i];// 订单项ID
            String address = form.getAddress()[i];
            int goodsQuantity = Integer.parseInt(form.getGoodsQuantity()[i]);// 商品数量
            double orderItemPrice = 0;// OrderItem总价
            Map<String, Object> goodsCondition = new HashMap<String, Object>();// 查询商品价格
            goodsCondition.put("goodsId", goodsId);
            ResultData goodsData = commodityService
                    .fetchGoods4Customer(goodsCondition);
            Goods4Agent goods = null;
            if (goodsData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<Goods4Agent> goodsList = (List) goodsData.getData();
                if (goodsList.size() != 1) {
                    Prompt prompt = new Prompt(PromptCode.WARNING, "提示",
                            "商品不唯一或未找到", "/agent/order/place");
                    attr.addFlashAttribute("prompt", prompt);
                    view.setViewName("redirect:/agent/prompt");
                    return view;
                }
                goods = goodsList.get(0);
            } else {
                Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "商品信息异常",
                        "/agent/order/place");
                attr.addFlashAttribute("prompt", prompt);
                view.setViewName("redirect:/agent/prompt");
                return view;
            }
            orderItemPrice = goods.getAgentPrice() * goodsQuantity;// 得到一个OrderItem的总价
            total_price += orderItemPrice;// 累加金额
            OrderItem orderItem = new OrderItem(customerId, goodsId,
                    goodsQuantity, orderItemPrice, address);// 构造OrderItem
            orderItem.setOrderItemId(orderItemId);// 传入OrderItemID
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);// 构造Order
        order.setPrice(total_price);
        order.setOrderId(form.getOrderId());
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

        ResultData fetchResponse = orderService.modifyOrder(order);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (type.equals("save")) {
                Prompt prompt = new Prompt("提示", "保存成功",
                        "/agent/order/manage/0");
                attr.addFlashAttribute("prompt", prompt);
                view.setViewName("redirect:/agent/prompt");
            } else if (type.equals("submit")) {
            	attr.addFlashAttribute("openId", openId);
                view.setViewName("redirect:/order/pay/" + order.getOrderId());
            }
            return view;
        }
        Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "失败",
                "/agent/order/manage/0");
        attr.addFlashAttribute("prompt", prompt);
        view.setViewName("redirect:/agent/prompt");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pay/{orderId}")
    public ModelAndView place(@PathVariable("orderId") String orderId) {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
        	WechatConfig.oauthWechat(view, "/agent/login");
            view.setViewName("/agent/login");
            return view;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("blockFlag", false);
        ResultData fetchAgentResponse = agentService.fetchAgent(condition);
        Agent agent = ((List<Agent>) fetchAgentResponse.getData()).get(0);
        condition.put("orderId", orderId);
        ResultData orderData = orderService.fetchOrder(condition);
        Order order = null;
        if (orderData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            order = ((List<Order>) orderData.getData()).get(0);
        } else {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "失败",
                    "/agent/order/manage/0");
            view.addObject("prompt", prompt);
            view.setViewName("redirect:/agent/prompt");
            return view;
        }
        condition.clear();
        condition.put("agentId", agent.getAgentId());
        Agent target = null;
        try {
            target = ((List<Agent>) agentService.fetchAgent(condition)
                    .getData()).get(0);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "失败",
                    "/agent/order/manage/0");
            view.addObject("prompt", prompt);
            view.setViewName("redirect:/agent/prompt");
            return view;
        }
        view.addObject("order", JSON.toJSON(order));
        view.addObject("agent", target);
        WechatConfig.oauthWechat(view, "agent/order/pay");
        view.setViewName("agent/order/pay");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cofferpay")
    public ModelAndView cofferPay(HttpServletRequest request,
                                  @Valid PayForm form, BindingResult result, RedirectAttributes attr) {
        ModelAndView view = new ModelAndView();
        String orderId = form.getOrderId();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
        	WechatConfig.oauthWechat(view, "/agent/login");
            view.setViewName("/agent/login");
            return view;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("orderId", orderId);
        ResultData orderData = orderService.fetchOrder(condition);
        Order order = null;
        try {
            order = ((List<Order>) orderData.getData()).get(0);
        } catch (Exception e) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "失败", null);
            attr.addFlashAttribute(prompt);
            view.setViewName("redirect:/agent/prompt");
            return view;
        }
        // 计算总价
        double totalPrice = order.getPrice();
        // 创建账单
        OrderBill orderBill = new OrderBill(totalPrice, "coffer",
                toolService.getIP(request), user.getAgent(), order);
        ResultData billData = billService.createOrderBill(orderBill, null);
        // 获取代理信息
        condition.clear();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("blockFlag", false);
        ResultData fetchAgentResponse = agentService.fetchAgent(condition);
        Agent agent = ((List<Agent>) fetchAgentResponse.getData()).get(0);
        if (billData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            ResultData cofferData = agentService.consume(agent, totalPrice);
            billService.updateOrderBill((OrderBill) billData.getData());
            if (cofferData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                // Order和OrderItem全部改成已付款
                order.setStatus(OrderStatus.PAYED);
                for (OrderItem orderItem : order.getOrderItems()) {
                    orderItem.setStatus(OrderItemStatus.PAYED);
                }
                ResultData payData = orderService.payOrder(order);
                if (payData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    Prompt prompt = new Prompt(PromptCode.SUCCESS, "付款成功",
                            "订单号：" + order.getOrderId() + "，请等待发货",
                            "/agent/order/manage/2");
                    attr.addFlashAttribute(prompt);
                    view.setViewName("redirect:/agent/prompt");
                    return view;
                }
            }
        }
        Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "失败",
                "/agent/order/manage/2");
        attr.addFlashAttribute(prompt);
        view.setViewName("redirect:/agent/prompt");
        return view;
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/otherpay")
    public Charge otherPay(HttpServletRequest request) {
        Charge charge = new Charge();
        JSONObject params = toolService.getParams(request);
        Subject subject = SecurityUtils.getSubject();
        String clientIp = toolService.getIP(request);
        User user = (User) subject.getPrincipal();
        if (user == null) {
            return null;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("orderId", String.valueOf(params.get("order_id")));
        ResultData orderData = orderService.fetchOrder(condition);
        Order order = null;
        if (orderData.getResponseCode() == ResponseCode.RESPONSE_OK
                && orderData.getData() != null) {
            order = ((List<Order>) orderData.getData()).get(0);
        }
        OrderBill bill = new OrderBill(Double.parseDouble(String.valueOf(params
                .get("amount"))), String.valueOf(params.get("channel")),
                clientIp, user.getAgent(), order);
        ResultData createResponse = billService.createOrderBill(bill, params.getString("open_id"));
        if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            charge = (Charge) createResponse.getData();
        }
        return charge;
    }

   

    @RequestMapping(method = RequestMethod.POST, value = "/customerpay")
    public Charge customerPay(HttpServletRequest request) {
        Charge charge = new Charge();
        JSONObject params = toolService.getParams(request);
        Subject subject = SecurityUtils.getSubject();
        String clientIp = toolService.getIP(request);
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("orderId", String.valueOf(params.get("order_id")));
        ResultData orderData = orderService.fetchCustomerOrder(condition);
        CustomerOrder customerOrder = null;
        if (orderData.getResponseCode() == ResponseCode.RESPONSE_OK
                && orderData.getData() != null) {
            customerOrder = ((List<CustomerOrder>) orderData.getData()).get(0);
        }

        CustomerOrderBill bill = new CustomerOrderBill(Double.parseDouble(String.valueOf(params
                .get("amount"))), String.valueOf(params.get("channel")),
                clientIp, customerOrder);
        ResultData createResponse = billService.createCustomerOrderBill(bill, params.getString("open_id"));
        if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            charge = (Charge) createResponse.getData();
        }
        return charge;
    }


}
