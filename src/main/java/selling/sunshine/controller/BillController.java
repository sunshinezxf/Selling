package selling.sunshine.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;

import selling.sunshine.model.Agent;
import selling.sunshine.model.BillStatus;
import selling.sunshine.model.DepositBill;
import selling.sunshine.model.Order;
import selling.sunshine.model.OrderBill;
import selling.sunshine.model.OrderItem;
import selling.sunshine.model.OrderItemStatus;
import selling.sunshine.model.OrderStatus;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.BillService;
import selling.sunshine.service.OrderService;
import selling.sunshine.service.ToolService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/10/16.
 */
@RequestMapping("/bill")
@RestController
public class BillController {

    private Logger logger = LoggerFactory.getLogger(BillController.class);
    
    @Autowired
    private ToolService toolService;
    
    @Autowired
    private BillService billService;
    
    @Autowired
    private AgentService agentService;
    
    @Autowired
    private OrderService orderService;


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/inform")
    public ResultData inform(HttpServletRequest request, HttpServletResponse response) throws IOException{
        ResultData resultData = new ResultData();
        
        JSONObject webhooks = toolService.getParams(request);
        logger.debug("webhooks info == " + webhooks);
        JSONObject charge = webhooks.getJSONObject("data").getJSONObject("object");
        logger.debug("charge info == " + charge);
        String dealId = charge.getString("order_no");
        logger.debug("deal id: " + dealId);
        
        //先处理错误状态
        if (StringUtils.isEmpty(dealId)) {
			resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
			return resultData;
		}
        Event event = Webhooks.eventParse(webhooks.toString());
        if ("charge.succeeded".equals(event.getType())) {
            response.setStatus(200);
        } else if ("refund.succeeded".equals(event.getType())) {
            response.setStatus(200);
        } else {
            response.setStatus(500);
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
			return resultData;
        }		
        
        if (dealId.startsWith("DPB")) {//充值的账单
        	Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("billId", dealId);
            resultData = billService.fetchDepositBill(condition);
            DepositBill depositBill = ((List<DepositBill>) resultData.getData()).get(0);
            
            Map<String, Object> agentCondition = new HashMap<String, Object>();
            agentCondition.put("agentId", depositBill.getAgent().getAgentId());
            Agent agent = ((List<Agent>)agentService.fetchAgent(agentCondition).getData()).get(0);
            agent.setCoffer(agent.getCoffer()+depositBill.getBillAmount());
            resultData = agentService.updateAgent(agent);
            depositBill.setStatus(BillStatus.PAYED);
            resultData = billService.updateDepositBill(depositBill);
            	
		} else if(dealId.startsWith("ODB")){//其他方式付款的账单
			//处理账单状态
			Map<String ,Object> condition = new HashMap<String, Object>();
			condition.put("billId", dealId);
			resultData = billService.fetchOrderBill(condition);
            OrderBill orderBill = ((List<OrderBill>) resultData.getData()).get(0);
            orderBill.setStatus(BillStatus.PAYED);
            resultData = billService.updateOrderBill(orderBill);
            
            //处理订单状态
            if(resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            	condition.clear();
        		condition.put("orderId", orderBill.getOrder().getOrderId());
        		ResultData orderData = orderService.fetchOrder(condition);
            	Order order = null;
        		if(orderData.getResponseCode() == ResponseCode.RESPONSE_OK && orderData.getData() != null) {
        			order = ((List<Order>)orderData.getData()).get(0);
        		}
        		double total_price_database = 0.0;
        		for(OrderItem orderItem : order.getOrderItems()) {
        			total_price_database += orderItem.getOrderItemPrice();
        		}
        		if(orderBill.getBillAmount() >= total_price_database) {
        			for(OrderItem orderItem : order.getOrderItems()) {
            			orderItem.setStatus(OrderItemStatus.PAYED);
            		}
        			order.setStatus(OrderStatus.PAYED);
        			ResultData payData = orderService.payOrder(order);
        		}
            }
		}
        
        return resultData;
    }
}
