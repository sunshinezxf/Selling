package selling.sunshine.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import common.sunshine.utils.SortRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import selling.sunshine.model.sum.AgentGoods;
import selling.sunshine.model.sum.OrderSeries;
import selling.sunshine.model.sum.OrderStatistics;
import selling.sunshine.model.sum.Vendition;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.CustomerService;
import selling.sunshine.service.OrderService;
import selling.sunshine.service.StatisticService;
import selling.sunshine.utils.BaiduMapUtils;
import selling.sunshine.vo.agent.AgentPurchase;
import selling.sunshine.vo.order.OrderItemSum;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

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

	@Autowired
	private CustomerService customerService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/order")
	public JSONObject order() {
		JSONObject result = new JSONObject();
		Map<String, Object> condition = new HashMap<>();
		List<Integer> status = new ArrayList<>();
		// 查询代理商订单的付款状态
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
		// 查询订单项的发货状态
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
		// 查询网络订单的发货状态
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
		ResultData resultData = statisticService.orderByYear();
		if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
			List<OrderSeries> list = (List<OrderSeries>) resultData.getData();
			JSONArray series = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				JSONArray data = new JSONArray();
				for (int j = 0; j < list.get(i).getData().length; j++) {
					data.add(list.get(i).getData()[j]);
				}
				JSONObject jsonObject = new JSONObject();
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
	public JSONObject purchaseRecordEveryday() {
		JSONObject result = new JSONObject();
		ResultData resultData = statisticService.purchaseRecordEveryday();
		result = (JSONObject) resultData.getData();
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/purchaseRecordEveryMonth")
	public JSONObject purchaseRecordEveryMonth() {
		JSONObject result = new JSONObject();
		ResultData resultData = statisticService.purchaseRecordEveryMonth();
		result = (JSONObject) resultData.getData();
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/purchaseRecordEveryday2")
	public JSONObject purchaseRecordEveryday2() {
		JSONObject result = new JSONObject();
		ResultData resultData = statisticService.purchaseRecordEveryday2();
		result = (JSONObject) resultData.getData();
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/purchaseRecordEveryMonth2")
	public JSONObject purchaseRecordEveryMonth2() {
		JSONObject result = new JSONObject();
		ResultData resultData = statisticService.purchaseRecordEveryMonth2();
		result = (JSONObject) resultData.getData();
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/sales/area")
	public ResultData salesArea() throws IOException {
		ResultData resultData = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		ResultData queryData = customerService.fetchCustomer(condition);
		if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
			List<Customer> customers = (List<Customer>) queryData.getData();
			Map<String, Integer> map = new HashMap<>();
			for (Customer customer : customers) {
				Map<String, BigDecimal> map1 = BaiduMapUtils.getLatAndLngByAddress(customer.getAddress().getAddress());
				BigDecimal lat = map1.get("lat");
				BigDecimal lng = map1.get("lng");
				Map<String, String> map2 = BaiduMapUtils.getAddressByLatAndLng(String.valueOf(lat),
						String.valueOf(lng));				
				if (map2 != null) {
					String province = map2.get("province");					
					if (map.containsKey(province)) {
						map.put(province, map.get(province) + 1);
					} else {
						map.put(province, 1);
					}
				}
			}
			resultData.setData(map);
			return resultData;
		}
		return resultData;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/agent/ranking")
	public ResultData agentRanking() {
		ResultData resultData = new ResultData();
		Map<String, Object> condition = new HashMap<String, Object>();
		List<Integer> orderTypeList = new ArrayList<Integer>();
		orderTypeList.add(0);
		orderTypeList.add(2);
		condition.put("orderTypeList", orderTypeList);
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(1);
		statusList.add(2);
		statusList.add(3);
		statusList.add(4);
		statusList.add(5);
		statusList.add(6);
		condition.put("statusList", statusList);
		List<SortRule> orderBy = new ArrayList<>();
		orderBy.add(new SortRule("goods_id", "desc"));
		orderBy.add(new SortRule("agent_id", "desc"));
		condition.put("sort", orderBy);
		ResultData fetchOrderItemSumResponse = orderService.fetchOrderItemSum(condition);
		if (fetchOrderItemSumResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
			return resultData;
		}
		// 从这里开始处理数据
		List<OrderItemSum> orderItemSumList = (List<OrderItemSum>) fetchOrderItemSumResponse.getData();
		Map<Goods4Agent, List<AgentPurchase>> goodsMap = new HashMap<>();
		Map<Goods4Agent, Integer> goodsQuantityMap = new HashMap<>();
		Goods4Agent goods = orderItemSumList.get(0).getGoods();
		goodsMap.put(goods, new ArrayList<>());
		common.sunshine.model.selling.agent.lite.Agent agent = orderItemSumList.get(0).getAgent();
		int quantity = 0;// 某商品下，某代理商购买数量
		int total_quantity = 0;// 某商品下，所有购买数量
		for (int i = 0; i < orderItemSumList.size(); i++) {
			OrderItemSum item = orderItemSumList.get(i);
			if (!goods.getGoodsId().equals(item.getGoods().getGoodsId())) {
				goodsQuantityMap.put(goods, total_quantity);
				goods = item.getGoods();
				List<AgentPurchase> agentPurchaseList = new ArrayList<>();
				goodsMap.put(goods, agentPurchaseList);
				total_quantity = 0;
			}
			if (item.getAgent() != null && (!agent.getAgentId().equals(item.getAgent().getAgentId())
					|| !goods.getGoodsId().equals(item.getGoods().getGoodsId()))) {
				AgentPurchase agentPurchase = new AgentPurchase(agent, quantity);
				goodsMap.get(goods).add(agentPurchase);
				agent = item.getAgent();
				quantity = 0;
			}
			if (item.getAgent() != null) {
				quantity += item.getGoodsQuantity();
			}
			total_quantity += item.getGoodsQuantity();
		}
		if (agent != null) {
			AgentPurchase agentPurchase = new AgentPurchase(agent, quantity);
			goodsMap.get(goods).add(agentPurchase);
		}
		goodsQuantityMap.put(goods, total_quantity);
		// 将数据处理成返回的格式
		JSONArray goodsArray = new JSONArray();
		for (Goods4Agent goods4Agent : goodsMap.keySet()) {
			JSONObject goodsObject = new JSONObject();
			goodsObject.put("goods_id", goods4Agent.getGoodsId());
			goodsObject.put("name", goods4Agent.getName());
			goodsObject.put("total_quantity", goodsQuantityMap.get(goods4Agent));
			JSONArray agentArray = new JSONArray();
			List<AgentPurchase> agentPurchaseList = goodsMap.get(goods4Agent);
			Collections.sort(agentPurchaseList, new Comparator() {
				@Override
				public int compare(Object o1, Object o2) {
					return ((AgentPurchase) o2).getQuantity() - ((AgentPurchase) o1).getQuantity();
				}
			});
			for (int i = 0; i < agentPurchaseList.size(); i++) {
				if (i < 10 || (agentPurchaseList.get(i).getQuantity() == agentPurchaseList.get(i - 1).getQuantity())) {
					JSONObject agentObject = new JSONObject();
					agentObject.put("agent_id", agentPurchaseList.get(i).getAgent().getAgentId());
					agentObject.put("agent_name", agentPurchaseList.get(i).getAgent().getName());
					agentObject.put("rank", i);
					agentObject.put("quantity", agentPurchaseList.get(i).getQuantity());
					agentArray.add(agentObject);
				} else {
					break;
				}
			}
			goodsObject.put("agent_info", agentArray);
			goodsArray.add(goodsObject);
		}
		resultData.setData(goodsArray);
		return resultData;
	}

	/**
	 * @param type,
	 *            type的取值daily, monthly, overall
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/sales/volume/{type}")
	public ResultData sales(@PathVariable("type") String type) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		if (type.equals("daily")) {
			condition.put("daily", true);
		} else if (type.equals("monthly")) {
			condition.put("monthly", true);
		}
		condition.put("type", 0);
		ResultData queryData = statisticService.purchaseRecord(condition);
		if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
			List<Vendition> goods = (List<Vendition>) queryData.getData();
			double price = 0.0;
			for (Vendition vendition : goods) {
				price += vendition.getRecordPrice();
			}
			result.setData(price);
			return result;
		} else if (queryData.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			return result;
		}
		result.setResponseCode(ResponseCode.RESPONSE_NULL);
		return result;
	}
}
