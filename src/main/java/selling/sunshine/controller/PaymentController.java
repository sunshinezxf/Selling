package selling.sunshine.controller;

import common.sunshine.model.selling.bill.CustomerOrderBill;
import common.sunshine.model.selling.bill.DepositBill;
import common.sunshine.model.selling.bill.OrderBill;
import common.sunshine.model.selling.bill.support.BillStatus;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.model.selling.order.support.OrderStatus;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.service.BillService;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.Prompt;
import selling.sunshine.utils.PromptCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付相关接口
 * Created by sunshine on 6/16/16.
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private BillService billService;
    
    @Autowired
    private OrderService orderService;

    /**
     * 使用支付宝付款后的订单状态页面
     *
     * @param billId
     * @param status
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{billId}/result/{status}")
    public ModelAndView prompt(@PathVariable("billId") String billId, @PathVariable("status") String status) {
        ModelAndView view = new ModelAndView();
        if (StringUtils.isEmpty(billId)) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您好,您当前的订单号异常,请重新尝试", "/commodity/list");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/prompt");
            return view;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("billId", billId);
        if (billId.startsWith("COB")) {
            ResultData response = billService.fetchCustomerOrderBill(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您当前的订单号异常,请重新尝试", "/commodity/list");
                view.addObject("prompt", prompt);
                view.setViewName("/customer/prompt");
                return view;
            }
            CustomerOrderBill bill = ((List<CustomerOrderBill>) response.getData()).get(0);
            if (bill.getStatus() == BillStatus.PAYED) {
                Prompt prompt = new Prompt("支付成功", "您的订单已付款成功,点击查看详情", "/commodity/customerorder?orderId=" + bill.getCustomerOrder().getOrderId());
                view.addObject("prompt", prompt);
                view.addObject("orderId", bill.getCustomerOrder().getOrderId());
                view.setViewName("/customer/prompt");
                return view;
            }
            if (bill.getStatus() == BillStatus.NOT_PAYED && (StringUtils.isEmpty(status) || (!StringUtils.isEmpty(status) && status.equals("success")))) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "支付处理中", "您的订单付款尚在处理中,请稍后核对订单状态", "/commodity/viewlist");
                view.addObject("prompt", prompt);
                view.setViewName("/customer/prompt");
                return view;
            }
            if (bill.getStatus() == BillStatus.NOT_PAYED && (!StringUtils.isEmpty(status) && status.equals("failure"))) {
                Prompt prompt = new Prompt(PromptCode.DANGER, "支付失败", "您已取消支付", "/commodity/viewlist");
                view.addObject("prompt", prompt);
                view.setViewName("/customer/prompt");
                return view;
            }
            Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您的订单尚未完成支付，请付款后进行查询", "/commodity/viewlist");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/prompt");
            return view;
        }
        if (billId.startsWith("ODB")) {
            ResultData response = billService.fetchOrderBill(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您当前的订单号异常,请重新尝试", "/agent/order/place");
                view.addObject("prompt", prompt);
                view.setViewName("/customer/prompt");
                return view;
            }
            OrderBill bill = ((List<OrderBill>) response.getData()).get(0);
            if (bill.getStatus() == BillStatus.PAYED) {
                Prompt prompt = new Prompt("支付成功", "您的订单已付款成功,点击查看订单列表", "/agent/order/manage/2");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            if (bill.getStatus() == BillStatus.NOT_PAYED && (StringUtils.isEmpty(status) || (!StringUtils.isEmpty(status) && status.equals("success")))) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "支付处理中", "您的订单付款尚在处理中,请稍后核对订单状态", "/agent/order/manage/2");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            if (bill.getStatus() == BillStatus.NOT_PAYED && (!StringUtils.isEmpty(status) && status.equals("failure"))) {
                Prompt prompt = new Prompt(PromptCode.DANGER, "支付失败", "您已取消付款", "/agent/order/place");
                view.addObject("prompt", prompt);
                return view;
            }
            Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您的订单尚未完成支付,请付款后进行查询", "/agent/order/place");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
        if (billId.startsWith("DPB")) {
            ResultData response = billService.fetchDepositBill(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您的账户充值失败,请重新尝试", "/account/deposit");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            DepositBill bill = ((List<DepositBill>) response.getData()).get(0);
            if (bill.getStatus() == BillStatus.PAYED) {
                Prompt prompt = new Prompt("支付成功", "您已成功充值", "/account/info");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            if (bill.getStatus() == BillStatus.NOT_PAYED && (StringUtils.isEmpty(status) || (!StringUtils.isEmpty(status) && status.equals("success")))) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "支付处理中", "您的充值尚在处理中,请稍后核对订单状态", "/account/info");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            if (bill.getStatus() == BillStatus.NOT_PAYED && (!StringUtils.isEmpty(status) && status.equals("failure"))) {
                Prompt prompt = new Prompt(PromptCode.DANGER, "支付失败", "您已取消付款", "/agent/account/info");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您的充值尚未完成支付,请付款后进行查询", "/agent/order/place");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
        Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您好,您当前的订单号异常,请重新尝试", "/commodity/list");
        view.addObject("prompt", prompt);
        view.setViewName("/customer/prompt");
        return view;
    }

    /**
     * 跳转到系统内的订单支付页面（付款前最后一个页面，代理商和客户商城共用页面）
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value="/bill/{orderId}")
    public ModelAndView bill(HttpServletRequest request, @PathVariable("orderId") String orderId){
    	ModelAndView view = new ModelAndView();
    	if(StringUtils.isEmpty(orderId)){
    		Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您好,您当前的订单异常,请重新尝试", "/commodity/list");
	        view.addObject("prompt", prompt);
	        view.setViewName("/customer/prompt");
	        return view;
    	}
    	if(orderId.startsWith("ODR")) {
	    	Map<String, Object> condition = new HashMap<String, Object>();
	    	condition.put("orderId", orderId);
	    	ResultData fetchOrderResponse = orderService.fetchOrder(condition);
	    	if(fetchOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK){
				Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您好,您当前的订单异常,请重新尝试", "/agent/order/place");
		        view.addObject("prompt", prompt);
		        view.setViewName("/agent/prompt");
		        return view;
	    	}
	    	Order order = ((List<Order>)fetchOrderResponse.getData()).get(0);
	    	if(order.getStatus() != OrderStatus.SAVED && order.getStatus() != OrderStatus.SUBMITTED){
	    		Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您好,您当前的订单已经付过款", "/agent/order/place");
		        view.addObject("prompt", prompt);
		        view.setViewName("/agent/prompt");
		        return view;
	    	}
	    	view.addObject("charge_url", "/order/otherpay");
	    	view.addObject("return_url", "/agent/order/manage/2");
	    	double totalPrice = order.getPrice();
	    	if(order.getVouchers() != null){
	    		totalPrice = order.getTotalPrice();
	    	}
	    	view.addObject("total_price", totalPrice);
	    	view.addObject("orderId", orderId);
	    	view.addObject("type", "agent");
	    	view.setViewName("/customer/pay/bill");
	    	return view;
    	} else if(orderId.startsWith("CUO")){
    		Map<String, Object> condition = new HashMap<String, Object>();
    		condition.put("orderId", orderId);
    		ResultData fetchCustomerOrderResponse = orderService.fetchCustomerOrder(condition);
    		if(fetchCustomerOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK){
				Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您好,您当前的订单异常,请重新尝试", "/commodity/list");
		        view.addObject("prompt", prompt);
		        view.setViewName("/customer/prompt");
		        return view;
	    	}
    		CustomerOrder customerOrder = ((List<CustomerOrder>)fetchCustomerOrderResponse.getData()).get(0);
    		if(customerOrder.getStatus() != OrderItemStatus.NOT_PAYED) {
    			Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您好,您当前的订单已经付过款", "/commodity/list");
		        view.addObject("prompt", prompt);
		        view.setViewName("/customer/prompt");
		        return view;
    		}
    		if (request.getHeader("user-agent").toLowerCase().contains("micromessenger")) {
            	HttpSession session = request.getSession();
                if (session.getAttribute("openId") != null && !session.getAttribute("openId").equals("")) {
                    String openId = (String) session.getAttribute("openId");
                    view.addObject("wechat", openId);
                } else {
                	Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您好,当前操作已超时,请重新下单", "/commodity/list");
    		        view.addObject("prompt", prompt);
    		        view.setViewName("/customer/prompt");
    		        return view;
                }
            }
    		view.addObject("charge_url", "/order/customerpay");
    		view.addObject("return_url", "/commodity/customerorder?orderId=" + orderId);
	    	view.addObject("total_price", customerOrder.getTotalPrice());
	    	view.addObject("orderId", orderId);
	    	view.addObject("type","customer");
	    	view.setViewName("/customer/pay/bill");
	    	return view;
    	} else {
    		Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您好,您当前的订单异常,请重新尝试", "/commodity/list");
	        view.addObject("prompt", prompt);
	        view.setViewName("/customer/prompt");
	        return view;
    	}
    }
}
