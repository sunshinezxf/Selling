package selling.sunshine.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import selling.sunshine.model.sum.AgentGoods;
import selling.sunshine.model.sum.OrderSeries;
import selling.sunshine.model.sum.OrderStatistics;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.OrderService;
import selling.sunshine.service.StatisticService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 6/14/16.
 */
@RestController
@RequestMapping("/statistic")
public class StatisticController {
    private Logger logger = LoggerFactory.getLogger(StatisticController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private StatisticService statisticService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/order")
    public JSONObject order() {
        JSONObject result = new JSONObject();
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        //查询代理商订单的付款状态
        int orderNotPayed = 0;
        int orderPayed = 0;
        status.add(1);
        condition.put("status", status);
        condition.put("blockFlag", false);
        ResultData fetchResponse = orderService.fetchOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            orderNotPayed = ((List) fetchResponse.getData()).size();
        }
        status.clear();
        condition.clear();
        status.add(2);
        status.add(3);
        status.add(4);
        condition.put("status", status);
        condition.put("blockFlag", false);
        fetchResponse = orderService.fetchOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            orderPayed = ((List) fetchResponse.getData()).size();
        }
        JSONArray payState4AgentOrder = new JSONArray();
        payState4AgentOrder.add(orderNotPayed);
        payState4AgentOrder.add(orderPayed);
        result.put("pay", payState4AgentOrder);
        //查询订单项的发货状态
        condition.clear();
        int orderItemNotDelivered = 0;
        int orderItemDelivered = 0;
        condition.put("status", 1);
        condition.put("blockFlag", false);
        fetchResponse = orderService.fetchOrderItem(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            orderItemNotDelivered = ((List) fetchResponse.getData()).size();
        }
        condition.clear();
        condition.put("status", 2);
        condition.put("blockFlag", false);
        fetchResponse = orderService.fetchOrderItem(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            orderItemDelivered = ((List) fetchResponse.getData()).size();
        }
        JSONArray deliverStatus4OrderItem = new JSONArray();
        deliverStatus4OrderItem.add(orderItemNotDelivered);
        deliverStatus4OrderItem.add(orderItemDelivered);
        result.put("deliver4OrderItem", deliverStatus4OrderItem);
        //查询网络订单的发货状态
        condition.clear();
        status.clear();
        int cusOrderNotDelivered = 0;
        int cusOrderDelivered = 0;
        status.add(1);
        condition.put("status", status);
        condition.put("blockFlag", false);
        fetchResponse = orderService.fetchCustomerOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            cusOrderNotDelivered = ((List) fetchResponse.getData()).size();
        }
        condition.clear();
        status.clear();
        status.add(2);
        condition.put("status", status);
        fetchResponse = orderService.fetchCustomerOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            cusOrderDelivered = ((List) fetchResponse.getData()).size();
        }
        JSONArray deliver4CusOrder = new JSONArray();
        deliver4CusOrder.add(cusOrderNotDelivered);
        deliver4CusOrder.add(cusOrderDelivered);
        result.put("deliver4Cus", deliver4CusOrder);
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/agent")
    public JSONObject agent() {
        JSONObject result = new JSONObject();
        Map<String, Object> condition = new HashMap<>();
        condition.put("granted", false);
        condition.put("blockFlag", false);
        int grantedNum = 0;
        int checkNum = 0;
        ResultData fetchResponse = agentService.fetchAgent(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            checkNum = ((List) fetchResponse.getData()).size();
        }
        condition.put("granted", true);
        fetchResponse = agentService.fetchAgent(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            grantedNum = ((List) fetchResponse.getData()).size();
        }
        JSONArray agentCheck = new JSONArray();
        agentCheck.add(grantedNum);
        agentCheck.add(checkNum);
        result.put("grant", agentCheck);
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/order/sum")
    public JSONObject orderSum() {
        JSONObject result = new JSONObject();
        ResultData queryResponse = statisticService.query4OrderSum();
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (JSONObject) queryResponse.getData();
        }
        logger.debug("data sum: " + result);
        return result;
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/order/month")
    public DataTablePage<OrderStatistics> orderMonth(DataTableParam param) {
    	 DataTablePage<OrderStatistics> result = new DataTablePage<>();
         if (StringUtils.isEmpty(param)) {
             return result;
         }
         ResultData fetchResponse = statisticService.orderStatisticsByPage(param);
         if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
             result = (DataTablePage<OrderStatistics>) fetchResponse.getData();
         }
         return result;
    } 
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/agent/goods/month")
    public DataTablePage<AgentGoods> agentGoodsMonth(DataTableParam param) {
    	 DataTablePage<AgentGoods> result = new DataTablePage<>();
         if (StringUtils.isEmpty(param)) {
             return result;
         }
         ResultData fetchResponse = statisticService.agentGoodsMonthByPage(param);
         if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
             result = (DataTablePage<AgentGoods>) fetchResponse.getData();
         }
         return result;
    } 
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/agent/goods")
    public DataTablePage<AgentGoods> agentGoods(DataTableParam param) {
    	 DataTablePage<AgentGoods> result = new DataTablePage<>();
         if (StringUtils.isEmpty(param)) {
             return result;
         }
         ResultData fetchResponse = statisticService.agentGoodsByPage(param);
         if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
             result = (DataTablePage<AgentGoods>) fetchResponse.getData();
         }
         return result;
    } 
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/orderByYear")
    public JSONObject orderByYear() {
    	JSONObject result = new JSONObject();
        ResultData resultData=statisticService.orderByYear();
        if (resultData.getResponseCode()==ResponseCode.RESPONSE_OK) {
			List<OrderSeries> list=(List<OrderSeries>)resultData.getData();
			JSONArray series=new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				JSONArray data=new JSONArray();
				for (int j = 0; j < list.get(i).getData().length; j++) {
					data.add(list.get(i).getData()[j]);
				}
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("name", list.get(i).getName());
				jsonObject.put("data", data);
				series.add(jsonObject);
			}
			result.put("series", series); 	
		}
        return result;
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/purchaseRecordEveryday")
    public JSONObject purchaseRecordEveryday(){
    	JSONObject result = new JSONObject();
        ResultData resultData=statisticService.purchaseRecordEveryday();
        result=(JSONObject)resultData.getData();
        return result;
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/purchaseRecordEveryMonth")
    public JSONObject purchaseRecordEveryMonth(){
    	JSONObject result = new JSONObject();
        ResultData resultData=statisticService.purchaseRecordEveryMonth();
        result=(JSONObject)resultData.getData();
        return result;
    }
    
    
}
