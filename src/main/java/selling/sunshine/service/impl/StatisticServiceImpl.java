package selling.sunshine.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.CommodityDao;
import selling.sunshine.dao.StatisticDao;
import selling.sunshine.model.sum.OrderMonth;
import selling.sunshine.model.sum.Sum4Order;
import selling.sunshine.model.sum.Vendition;
import selling.sunshine.model.sum.Volume;
import selling.sunshine.service.StatisticService;

import java.util.*;

/**
 * Created by sunshine on 6/24/16.
 */
@Service
public class StatisticServiceImpl implements StatisticService {
    private Logger logger = LoggerFactory.getLogger(StatisticServiceImpl.class);

    @Autowired
    private StatisticDao statisticDao;

    @Autowired
    private CommodityDao commodityDao;

    @Override
    public ResultData query4OrderSum() {
        ResultData result = new ResultData();
        ResultData queryResponse = statisticDao.queryOrderSum();
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Sum4Order> list = ((List<Sum4Order>) queryResponse.getData());
            JSONObject record = new JSONObject();
            Set<Integer> quantity = new TreeSet<>();
            HashMap<String, List<Sum4Order>> map = new HashMap<>();
            for (Sum4Order item : list) {
                List<Sum4Order> l;
                if (map.get(item.getGoodsId()) == null || map.get(item.getGoodsId()).isEmpty()) {
                    l = new ArrayList<>();
                } else {
                    l = map.get(item.getGoodsId());
                }
                l.add(item);
                map.put(item.getGoodsId(), l);
                quantity.add(item.getQuantity());
            }
            JSONArray categories = new JSONArray();
            Iterator<Integer> it = quantity.iterator();
            while (it.hasNext()) {
                int temp = it.next();
                categories.add(temp + "盒");
            }
            record.put("category", categories);
            JSONArray series = new JSONArray();
            Iterator i = map.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry entry = (Map.Entry) i.next();
                List<Sum4Order> sum = (List<Sum4Order>) entry.getValue();
                JSONObject data = new JSONObject();
                data.put("name", sum.get(0).getGoodsName());
                JSONArray val = new JSONArray();
                Iterator<Integer> quantityIterator = quantity.iterator();
                int index = 0;
                while (quantityIterator.hasNext()) {
                    int temp = quantityIterator.next();
                    if (sum.size() >= index + 1) {
                        Sum4Order current = sum.get(index);
                        if (current.getQuantity() == temp) {
                            val.add(current.getNum());
                            index++;
                        } else {
                            val.add(0);
                        }
                    } else {
                        val.add(0);
                    }
                }
                data.put("data", val);
                series.add(data);
            }
            record.put("series", series);
            result.setData(record);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        return result;
    }

    @Override
    public ResultData orderStatisticsByPage(DataTableParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = statisticDao.orderStatisticsByPage(param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData agentGoodsMonthByPage(DataTableParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = statisticDao.agentGoodsMonthByPage(param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData agentGoodsByPage(DataTableParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = statisticDao.agentGoodsByPage(param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData orderMonth() {
        ResultData result = new ResultData();
        ResultData response = statisticDao.orderMonth();
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<OrderMonth>) response.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData orderLastYear(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = statisticDao.orderLastYear(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List) response.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData topThreeAgent() {
        ResultData result = new ResultData();
        ResultData response = statisticDao.topThreeAgent();
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List) response.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData purchaseRecord(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = statisticDao.purchaseRecord(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Vendition>) response.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchVolume(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = statisticDao.queryVolume(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Volume>) response.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(response.getData());
        } else {
            response.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData queryAgentGoods(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = statisticDao.queryAgentGoods(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Volume>) response.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(response.getData());
        } else {
            response.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData agentRanking(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = statisticDao.agentRanking(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            response.setDescription(response.getDescription());
        }
        return result;
    }

//    @Override
//    public ResultData purchaseRecordEveryday(Map<String, Object> condition) {
//        ResultData result = new ResultData();
//        ResultData response = statisticDao.purchaseRecordEveryday(condition);
//        List<Map<String, Object>> list = (List<Map<String, Object>>) response.getData();
//        if (list.size() == 0) {
//            return result;
//        }
//        condition.clear();
//        if (commodityDao.queryGoods4Agent(condition).getResponseCode() == ResponseCode.RESPONSE_OK) {
//            List<Goods4Agent> goods = (List<Goods4Agent>) commodityDao.queryGoods4Agent(condition).getData();
//            JSONArray categories = new JSONArray();
//            JSONArray series = new JSONArray();
//            int length = Integer.parseInt(((String) list.get(list.size() - 1).get("date")).substring(8, 10));
//            Map<String, int[]> goodsMap = new HashMap<>();
//            for (int i = 0; i < goods.size(); i++) {
//                goodsMap.put(goods.get(i).getName(), new int[length]);
//            }
//            for (int i = 0; i < length; i++) {
//                categories.add((i + 1) + "号");
//            }
//            for (int i = 0; i < list.size(); i++) {
//                Map<String, Object> map = list.get(i);
//                int[] data = goodsMap.get((String) map.get("goodsName"));
//                data[Integer.parseInt(((String) map.get("date")).substring(8, 10)) - 1] = Integer.parseInt(map.get("quantity").toString());
//                goodsMap.put((String) map.get("goodsName"), data);
//            }
//
//            for (String key : goodsMap.keySet()) {
//                JSONObject temp = new JSONObject();
//                JSONArray data = new JSONArray();
//                for (int i = 0; i < goodsMap.get(key).length; i++) {
//                    data.add(goodsMap.get(key)[i]);
//                }
//                temp.put("name", key);
//                temp.put("data", data);
//                series.add(temp);
//            }
//            JSONObject record = new JSONObject();
//            record.put("categories", categories);
//            record.put("series", series);
//            result.setData(record);
//        }
//
//        return result;
//    }
//
//    @Override
//    public ResultData purchaseRecordEveryMonth(Map<String, Object> condition) {
//        ResultData result = new ResultData();
//        ResultData response = statisticDao.purchaseRecordEveryMonth(condition);
//        List<Map<String, Object>> list = (List<Map<String, Object>>) response.getData();
//        if (list.size() == 0) {
//            return result;
//        }
//        condition.clear();
//        if (commodityDao.queryGoods4Agent(condition).getResponseCode() == ResponseCode.RESPONSE_OK) {
//            List<Goods4Agent> goods = (List<Goods4Agent>) commodityDao.queryGoods4Agent(condition).getData();
//            JSONArray categories = new JSONArray();
//            JSONArray series = new JSONArray();
//            int length = Integer.parseInt(((String) list.get(list.size() - 1).get("date")).substring(5, 7));
//            Map<String, int[]> goodsMap = new HashMap<>();
//            for (int i = 0; i < goods.size(); i++) {
//                goodsMap.put(goods.get(i).getName(), new int[length]);
//            }
//            for (int i = 0; i < length; i++) {
//                categories.add((i + 1) + "月");
//            }
//            for (int i = 0; i < list.size(); i++) {
//                Map<String, Object> map = list.get(i);
//                int[] data = goodsMap.get((String) map.get("goodsName"));
//                data[Integer.parseInt(((String) map.get("date")).substring(5, 7)) - 1] = Integer.parseInt(map.get("quantity").toString());
//                goodsMap.put((String) map.get("goodsName"), data);
//            }
//
//            for (String key : goodsMap.keySet()) {
//                JSONObject temp = new JSONObject();
//                JSONArray data = new JSONArray();
//                for (int i = 0; i < goodsMap.get(key).length; i++) {
//                    data.add(goodsMap.get(key)[i]);
//                }
//                temp.put("name", key);
//                temp.put("data", data);
//                series.add(temp);
//            }
//            JSONObject record = new JSONObject();
//            record.put("categories", categories);
//            record.put("series", series);
//            result.setData(record);
//        }
//
//        return result;
//    }
//
//    @Override
//    public ResultData purchaseRecordEveryday2(Map<String, Object> condition) {
//        ResultData result = new ResultData();
//        ResultData response = statisticDao.purchaseRecordEveryday(condition);
//        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
//            return result;
//        }
//        List<Map<String, Object>> list = (List<Map<String, Object>>) response.getData();
//        if (list.isEmpty()) {
//            return result;
//        }
//        condition.clear();
//        if (commodityDao.queryGoods4Agent(condition).getResponseCode() == ResponseCode.RESPONSE_OK) {
//            List<Goods4Agent> goods = (List<Goods4Agent>) commodityDao.queryGoods4Agent(condition).getData();
//            JSONArray categories = new JSONArray();
//            JSONArray series = new JSONArray();
//            int length = Integer.parseInt(((String) list.get(list.size() - 1).get("date")).substring(8, 10));
//            Map<String, double[]> goodsMap = new HashMap<>();
//            for (int i = 0; i < goods.size(); i++) {
//                goodsMap.put(goods.get(i).getName(), new double[length]);
//            }
//            for (int i = 0; i < length; i++) {
//                categories.add((i + 1) + "号");
//            }
//            for (int i = 0; i < list.size(); i++) {
//                Map<String, Object> map = list.get(i);
//                double[] data = goodsMap.get((String) map.get("goodsName"));
//                data[Integer.parseInt(((String) map.get("date")).substring(8, 10)) - 1] = Double.parseDouble(map.get("price").toString());
//                goodsMap.put((String) map.get("goodsName"), data);
//            }
//
//            for (String key : goodsMap.keySet()) {
//                JSONObject temp = new JSONObject();
//                JSONArray data = new JSONArray();
//                for (int i = 0; i < goodsMap.get(key).length; i++) {
//                    data.add(goodsMap.get(key)[i]);
//                }
//                temp.put("name", key);
//                temp.put("data", data);
//                series.add(temp);
//            }
//            JSONObject record = new JSONObject();
//            record.put("categories", categories);
//            record.put("series", series);
//            result.setData(record);
//        }
//
//        return result;
//    }
//
//    @Override
//    public ResultData purchaseRecordEveryMonth2(Map<String, Object> condition) {
//        ResultData result = new ResultData();
//        ResultData response = statisticDao.purchaseRecordEveryMonth(condition);
//        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
//            return result;
//        }
//        List<Map<String, Object>> list = (List<Map<String, Object>>) response.getData();
//        condition.clear();
//        if (commodityDao.queryGoods4Agent(condition).getResponseCode() == ResponseCode.RESPONSE_OK) {
//            List<Goods4Agent> goods = (List<Goods4Agent>) commodityDao.queryGoods4Agent(condition).getData();
//            JSONArray categories = new JSONArray();
//            JSONArray series = new JSONArray();
//            int length = Integer.parseInt(((String) list.get(list.size() - 1).get("date")).substring(5, 7));
//            Map<String, double[]> goodsMap = new HashMap<>();
//            for (int i = 0; i < goods.size(); i++) {
//                goodsMap.put(goods.get(i).getName(), new double[length]);
//            }
//            for (int i = 0; i < length; i++) {
//                categories.add((i + 1) + "月");
//            }
//            for (int i = 0; i < list.size(); i++) {
//                Map<String, Object> map = list.get(i);
//                double[] data = goodsMap.get((String) map.get("goodsName"));
//                data[Integer.parseInt(((String) map.get("date")).substring(5, 7)) - 1] = Double.parseDouble(map.get("price").toString());
//                goodsMap.put((String) map.get("goodsName"), data);
//            }
//
//            for (String key : goodsMap.keySet()) {
//                JSONObject temp = new JSONObject();
//                JSONArray data = new JSONArray();
//                for (int i = 0; i < goodsMap.get(key).length; i++) {
//                    data.add(goodsMap.get(key)[i]);
//                }
//                temp.put("name", key);
//                temp.put("data", data);
//                series.add(temp);
//            }
//            JSONObject record = new JSONObject();
//            record.put("categories", categories);
//            record.put("series", series);
//            result.setData(record);
//        }
//
//        return result;
//    }

	@Override
	public ResultData perGoodsPurchaseRecordMonth(Map<String, Object> condition) {
		ResultData result = new ResultData();
        ResultData response = statisticDao.purchaseRecordEveryMonth(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
        	result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        List<Map<String, Object>> list = (List<Map<String, Object>>) response.getData();
        JSONArray categories = new JSONArray();
        JSONArray quantitySeriesData = new JSONArray();
        JSONArray priceSeriesData = new JSONArray();
        
        for (int i = 0; i < list.size(); i++) {
        	String date=(String)list.get(i).get("date");
        	String[] dates=date.split("-");
			categories.add(dates[0]+"年"+Integer.parseInt(dates[1])+"月");
			quantitySeriesData.add(list.get(i).get("quantity"));
			priceSeriesData.add(list.get(i).get("price"));
		}
        JSONObject record = new JSONObject();
        record.put("categories", categories);
        record.put("quantitySeriesData", quantitySeriesData);
        record.put("priceSeriesData", priceSeriesData);
        result.setData(record);
		return result;
	}

	@Override
	public ResultData perGoodsPurchaseRecordDay(Map<String, Object> condition) {
		ResultData result = new ResultData();
        ResultData response = statisticDao.purchaseRecordEveryday(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
        	result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        List<Map<String, Object>> list = (List<Map<String, Object>>) response.getData();
        JSONArray categories = new JSONArray();
        JSONArray quantitySeriesData = new JSONArray();
        JSONArray priceSeriesData = new JSONArray();
        
        for (int i = 0; i < list.size(); i++) {
        	String date=(String)list.get(i).get("date");
        	String[] dates=date.split("-");
			categories.add(Integer.parseInt(dates[2])+"号");
			quantitySeriesData.add(list.get(i).get("quantity"));
			priceSeriesData.add(list.get(i).get("price"));
		}
        JSONObject record = new JSONObject();
        record.put("categories", categories);
        record.put("quantitySeriesData", quantitySeriesData);
        record.put("priceSeriesData", priceSeriesData);
        result.setData(record);
		return result;
	}


}
