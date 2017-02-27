package selling.sunshine.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import common.sunshine.utils.SortRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import selling.sunshine.model.sum.Vendition;
import selling.sunshine.service.*;
import selling.sunshine.vo.agent.AgentPurchase;
import selling.sunshine.vo.customer.CustomerVo;
import selling.sunshine.vo.order.OrderItemSum;
import selling.sunshine.vo.sum.SalesVo;

import java.io.IOException;
import java.util.*;

/**
 * 专门用来提供统计信息的接口
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

    @Autowired
    private CommodityService commodityService;

    /**
     * 获取订单中的商品汇总信息
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/order/check")
    public ResultData checkOrder() {
        ResultData result = new ResultData();
        ResultData response = statisticService.query4OrderSum();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前暂无订单中的商品汇总信息");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("获取订单中的商品汇总信息错误");
        }
        return result;
    }

    /**
     * 近一年订单笔数
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/order/summary")
    public ResultData orderLastYear() {
        ResultData result = new ResultData();
        JSONObject dataObject = new JSONObject();
        Map<String, Object> condition = new HashMap<String, Object>();
        ResultData responseAll = statisticService.orderLastYear(condition);
        if (responseAll.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) responseAll.getData();
            JSONArray series = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("time", list.get(i).get("date"));
                jsonObject.put("quantity", list.get(i).get("amount"));
                series.add(jsonObject);
            }
            dataObject.put("ALL", series);
        }
        condition.clear();
        condition.put("orderType", 0);
        ResultData responseOrdinary = statisticService.orderLastYear(condition);
        if (responseOrdinary.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) responseOrdinary.getData();
            JSONArray series = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("time", list.get(i).get("date"));
                jsonObject.put("quantity", list.get(i).get("amount"));
                series.add(jsonObject);
            }
            dataObject.put("ORDINARY", series);
        }
        condition.clear();
        condition.put("orderType", 1);
        ResultData responseGift = statisticService.orderLastYear(condition);
        if (responseGift.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) responseGift.getData();
            JSONArray series = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("time", list.get(i).get("date"));
                jsonObject.put("quantity", list.get(i).get("amount"));
                series.add(jsonObject);
            }
            dataObject.put("GIFT", series);
        }
        result.setData(dataObject);
        return result;
    }

    /**
     * 全国客户区域分布
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/sales/area")
    public ResultData salesArea() throws IOException {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData queryData = customerService.fetchCustomer(condition);
        if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CustomerVo> customers = (List<CustomerVo>) queryData.getData();
            Map<String, Integer> map = new HashMap<>();
            for (CustomerVo customer : customers) {
                String province = customer.getProvince();
                if (province != null) {
                    if (map.containsKey(province)) {
                        map.put(province, map.get(province) + 1);
                    } else {
                        map.put(province, 1);
                    }
                }
            }
            JSONArray array = new JSONArray();
            for (String key : map.keySet()) {
                JSONObject object = new JSONObject();
                object.put("name", key);
                object.put("value", map.get(key));
                array.add(object);
            }
            resultData.setData(array);
            return resultData;
        }
        return resultData;
    }

    /**
     * 各个商品的代理商销售排名情况
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/agent/rank")
    public ResultData agentRanking() {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        List<Integer> orderTypeList = new ArrayList<>();
        orderTypeList.add(0);
        orderTypeList.add(2);
        condition.put("orderTypeList", orderTypeList);
        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);
        statusList.add(2);
        statusList.add(3);
        statusList.add(4);
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
                //goods = item.getGoods();
                List<AgentPurchase> agentPurchaseList = new ArrayList<>();
                goodsMap.put(item.getGoods(), agentPurchaseList);
                total_quantity = 0;
            }
            if (item.getAgent() != null && (!agent.getAgentId().equals(item.getAgent().getAgentId())
                    || !goods.getGoodsId().equals(item.getGoods().getGoodsId()))) {
                AgentPurchase agentPurchase = new AgentPurchase(agent, quantity);
                goodsMap.get(goods).add(agentPurchase);
                agent = item.getAgent();
                quantity = 0;
                if (!goods.getGoodsId().equals(item.getGoods().getGoodsId())) {
                    goods = item.getGoods();
                }
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
                if (i < 5 || (agentPurchaseList.get(i).getQuantity() == agentPurchaseList.get(i - 1).getQuantity())) {
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
     * 当日、当月、累计销售金额
     * @param type, type的取值daily, monthly, overall
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

    /**
     * 统计每个商品的每月购买信息
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/goods/purchaseRecord/month")
    public ResultData perGoodsPurchaseRecordMonth() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        ResultData fetchResponse = commodityService.fetchGoods4Agent(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Goods4Agent> goods = (List<Goods4Agent>) fetchResponse.getData();
            JSONArray goodsArray = new JSONArray();
            for (Goods4Agent goods4Agent : goods) {
                condition.put("goodsId", goods4Agent.getGoodsId());
                fetchResponse = statisticService.perGoodsPurchaseRecordMonth(condition);
                if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    JSONObject record = (JSONObject) fetchResponse.getData();
                    if (StringUtils.isEmpty(goods4Agent.getNickname())) {
                        record.put("name", goods4Agent.getName());
                    } else {
                        record.put("name", goods4Agent.getNickname());
                    }
                    record.put("id", goods4Agent.getGoodsId());
                    goodsArray.add(record);
                }
            }
            result.setData(goodsArray);
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        return result;
    }

    /**
     * 统计每个商品的每天购买信息
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/goods/purchaseRecord/day")
    public ResultData perGoodsPurchaseRecordDay() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        ResultData fetchResponse = commodityService.fetchGoods4Agent(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Goods4Agent> goods = (List<Goods4Agent>) fetchResponse.getData();
            JSONArray goodsArray = new JSONArray();
            for (Goods4Agent goods4Agent : goods) {
                condition.put("goodsId", goods4Agent.getGoodsId());
                fetchResponse = statisticService.perGoodsPurchaseRecordDay(condition);
                if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    JSONObject record = (JSONObject) fetchResponse.getData();
                    if (StringUtils.isEmpty(goods4Agent.getNickname())) {
                        record.put("name", goods4Agent.getName());
                    } else {
                        record.put("name", goods4Agent.getNickname());
                    }
                    record.put("id", goods4Agent.getGoodsId());
                    goodsArray.add(record);
                }
            }
            result.setData(goodsArray);
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        return result;
    }

    /**
     * 获取近12个月每月平台的销售金额数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/sales")
    public ResultData sales() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        ResultData response = statisticService.fetchSales(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            //该list中的数据为按照日期倒序排列的
            List<SalesVo> list = (List<SalesVo>) response.getData();
            String[] month = new String[list.size()];
            double[] price = new double[list.size()];
            for (int i = list.size() - 1; i >= 0; i--) {
                month[list.size() - 1 - i] = list.get(i).getCreateAt();
                price[list.size() - 1 - i] = list.get(i).getPrice();
            }
            JSONObject json = new JSONObject();
            json.put("month", month);
            json.put("price", price);
            result.setData(json);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

}
