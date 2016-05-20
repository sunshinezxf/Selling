package selling.sunshine.controller;

import org.apache.shiro.SecurityUtils;

import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pingplusplus.model.Charge;

import selling.sunshine.form.OrderItemForm;
import selling.sunshine.form.PayForm;
import selling.sunshine.form.SortRule;
import selling.sunshine.model.Agent;
import selling.sunshine.model.Bill;
import selling.sunshine.model.DepositBill;
import selling.sunshine.model.Goods;
import selling.sunshine.model.Order;
import selling.sunshine.model.OrderBill;
import selling.sunshine.model.OrderItem;
import selling.sunshine.model.OrderItemStatus;
import selling.sunshine.model.OrderStatus;
import selling.sunshine.model.Role;
import selling.sunshine.model.User;
import selling.sunshine.pagination.MobilePage;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.BillService;
import selling.sunshine.service.CommodityService;
import selling.sunshine.service.OrderService;
import selling.sunshine.service.ToolService;
import selling.sunshine.utils.Prompt;
import selling.sunshine.utils.PromptCode;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
        condition.put("status", OrderStatus.PAYED.getCode());
        List<SortRule> rule = new ArrayList<SortRule>();
        rule.add(new SortRule("create_time", "asc"));
        condition.put("sort", rule);
        ResultData fetchResponse = orderService.fetchOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (MobilePage<Order>) fetchResponse.getData();
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
        condition.put("status", param.getParams().get("status"));
        ResultData fetchResponse = orderService.fetchOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (MobilePage<Order>) fetchResponse.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/express")
    public ModelAndView express() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/express");
        return view;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/orderItem/{orderId}")
    public ModelAndView overviewOrderItem(@PathVariable("orderId") String orderId) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        ResultData orderData=orderService.fetchOrder(condition);
        Order order=((List<Order>)orderData.getData()).get(0);
        view.addObject("order",order);
        double totalPrices=0.0;
        Map<String, Object> goods_quantity_Map= new HashMap<>();
        for(OrderItem item:order.getOrderItems()){
        	totalPrices+=item.getOrderItemPrice();
        	String goodsName=item.getGoods().getName();
        	int goodsQuantity=item.getGoodsQuantity();
        	if (goods_quantity_Map.containsKey(goodsName)) {
				goods_quantity_Map.put(goodsName, ((int)goods_quantity_Map.get(goodsName)+goodsQuantity));
			}else{
				goods_quantity_Map.put(goodsName, goodsQuantity);
			}
        }
        view.addObject("totalPrices",totalPrices);
        view.addObject("goods_quantity_Map",goods_quantity_Map);
        view.setViewName("/backend/order/orderItem");
        return view;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/save")
    public ModelAndView save() {
        ModelAndView view = new ModelAndView();
        view.setViewName("");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/modify/{type}")
    public ModelAndView modifyOrder(@Valid OrderItemForm form, BindingResult result, RedirectAttributes attr, @PathVariable("type") String type) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/order/list/0");
            return view;
        }
        Subject subject = SecurityUtils.getSubject();
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        int length = form.getCustomerId().length;
        Order order = new Order();
        Agent agent = new Agent();
        User user = (User) subject.getPrincipal();
        agent.setAgentId(user.getAgent().getAgentId());
        order.setAgent(agent);
        for (int i = 0; i < length; i++) {
            String goodsId = form.getGoodsId()[i];//商品ID
            String customerId = form.getCustomerId()[i];//顾客ID
            String orderItemId = form.getOrderItemId()[i];//订单项ID
            int goodsQuantity = Integer.parseInt(form.getGoodsQuantity()[i]);//商品数量
            double orderItemPrice = 0;//OrderItem总价
            Map<String, Object> goodsCondition = new HashMap<String, Object>();//查询商品价格
            goodsCondition.put("goodsId", goodsId);
            ResultData goodsData = commodityService.fetchCommodity(goodsCondition);
            Goods goods = null;
            if (goodsData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<Goods> goodsList = (List<Goods>) goodsData.getData();
                if (goodsList.size() != 1) {
                    Prompt prompt = new Prompt();
                    prompt.setCode(PromptCode.WARNING);
                    prompt.setMessage("商品不唯一或未找到");
                    attr.addFlashAttribute("prompt", prompt);
                    view.setViewName("redirect:/agent/prompt");
                    return view;
                }
                goods = goodsList.get(0);
            } else {
                Prompt prompt = new Prompt();
                prompt.setCode(PromptCode.WARNING);
                prompt.setMessage("商品信息异常");
                attr.addFlashAttribute("prompt", prompt);
                view.setViewName("redirect:/agent/prompt");
                return view;
            }
            orderItemPrice = goods.getPrice() * goodsQuantity;//得到一个OrderItem的总价
            OrderItem orderItem = new OrderItem(customerId, goodsId, goodsQuantity, orderItemPrice);//构造OrderItem
            orderItem.setOrderItemId(orderItemId);//传入OrderItemID
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);//构造Order
        order.setOrderId(form.getOrderId());
        switch(type){
        case "save":order.setStatus(OrderStatus.SAVED);break;
        case "submit":order.setStatus(OrderStatus.SUBMITTED);break;
        default: order.setStatus(OrderStatus.SAVED);
        }
        
        ResultData fetchResponse = orderService.modifyOrder(order);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
        	if(type.equals("save")){
		        Prompt prompt = new Prompt();
		        prompt.setCode(PromptCode.SUCCESS);
		        prompt.setTitle("提示");
		        prompt.setConfirmURL("/agent/order/manage/0");
		    	prompt.setMessage("保存成功");
		    	attr.addFlashAttribute("prompt", prompt);
		        view.setViewName("redirect:/agent/prompt");
            } else if(type.equals("submit")){
            	view.setViewName("redirect:/order/pay/" + order.getOrderId());
            }
        	return view;
        }
        Prompt prompt = new Prompt();
        prompt.setCode(PromptCode.WARNING);
        prompt.setTitle("提示");
        prompt.setMessage("失败");
        attr.addFlashAttribute("prompt", prompt);
        view.setViewName("redirect:/agent/prompt");
        return view;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/pay/{orderId}")
    public ModelAndView place(@PathVariable("orderId") String orderId) {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        Agent agent = user.getAgent();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", agent.getAgentId());
        condition.put("orderId", orderId);
        ResultData orderData =  orderService.fetchOrder(condition);
        Order order = null;
        if(orderData.getResponseCode() == ResponseCode.RESPONSE_OK){
        	order = ((List<Order>) orderData.getData()).get(0);
        } else {
        	 Prompt prompt = new Prompt();
             prompt.setCode(PromptCode.WARNING);
             prompt.setTitle("提示");
             prompt.setMessage("失败");
             view.addObject("prompt", prompt);
             view.setViewName("redirect:/agent/prompt");
             return view;
        }
        view.addObject("order", order);
        view.addObject("agent", agent);
        view.setViewName("agent/order/pay");
        return view;
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/cofferpay")
    public ModelAndView cofferPay(HttpServletRequest request,@Valid PayForm form, BindingResult result, RedirectAttributes attr) {
    	ModelAndView view = new ModelAndView();
    	String orderId = form.getOrderId();
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		Agent agent = user.getAgent();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("agentId", agent.getAgentId());
		condition.put("orderId", orderId);
		ResultData orderData = orderService.fetchOrder(condition);
		Order order = null;
		try{
			order = ((List<Order>) orderData.getData()).get(0);
		} catch(Exception e){
			Prompt prompt = new Prompt();
            prompt.setCode(PromptCode.WARNING);
            prompt.setTitle("提示");
            prompt.setMessage("失败");
            attr.addFlashAttribute(prompt);
            view.setViewName("redirect:/agent/prompt");
            return view;
		}
		//计算总价
		double totalPrice = 0.0;
    	for(OrderItem orderItem : order.getOrderItems()){
    		totalPrice += orderItem.getOrderItemPrice();
    	}
    	//创建账单
    	OrderBill orderBill = new OrderBill(totalPrice,"coffer",toolService.getIP(request),agent,order);
    	ResultData billData = billService.createOrderBill(orderBill);
    	if(billData.getResponseCode() == ResponseCode.RESPONSE_OK){
    		ResultData cofferData = agentService.consume(agent, totalPrice);
    		billService.updateOrderBill((OrderBill) billData.getData());
    		if(cofferData.getResponseCode() == ResponseCode.RESPONSE_OK){
    			//Order和OrderItem全部改成已付款
    			order.setStatus(OrderStatus.PAYED);
    			for(OrderItem orderItem : order.getOrderItems()) {
    				orderItem.setStatus(OrderItemStatus.PAYED);
    			}
    			ResultData payData = orderService.payOrder(order);
    			if(payData.getResponseCode() == ResponseCode.RESPONSE_OK) {
	        		Prompt prompt = new Prompt();
	                prompt.setCode(PromptCode.WARNING);
	                prompt.setTitle("付款成功");
	                prompt.setMessage("订单号：" + order.getOrderId() + "，请等待发货");
	                prompt.setConfirmURL("/agent/order/manage/2");
	                attr.addFlashAttribute(prompt);
	                view.setViewName("redirect:/agent/prompt");
	                return view;
                }
        	} 
    	}
    	Prompt prompt = new Prompt();
        prompt.setCode(PromptCode.WARNING);
        prompt.setTitle("提示");
        prompt.setMessage("失败");
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
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("orderId", String.valueOf(params.get("order_id")));
		ResultData orderData = orderService.fetchOrder(condition);
		Order order = null;
		if(orderData.getResponseCode() == ResponseCode.RESPONSE_OK && orderData.getData() != null) {
			order = ((List<Order>)orderData.getData()).get(0);
		}
		OrderBill bill = new OrderBill(Double.parseDouble(String.valueOf(params.get("amount"))), String.valueOf(params.get("channel")), clientIp, user.getAgent(), order);
		ResultData createResponse = billService.createOrderBill(bill);
		if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			charge = (Charge) createResponse.getData();
		}
		return charge;
	}
}
