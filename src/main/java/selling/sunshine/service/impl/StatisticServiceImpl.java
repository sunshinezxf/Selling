package selling.sunshine.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.hibernate.validator.constraints.Length;
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
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.pagination.DataTableParam;
import selling.sunshine.service.StatisticService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

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
        logger.debug("sssss: " + JSON.toJSONString(result));
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
    public ResultData orderByYear() {
        ResultData result = new ResultData();
        ResultData response = statisticDao.orderByYear();
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

	@Override
	public ResultData purchaseRecordEveryday() {
		ResultData result = new ResultData();
		ResultData response =statisticDao.purchaseRecordEveryday();
		List<Map<String, Object>> list =(List<Map<String, Object>>) response.getData();
		if (list.size()==0) {
			return result;
		}
		Map<String, Object> condition=new HashMap<>();
		if (commodityDao.queryGoods4Agent(condition).getResponseCode()==ResponseCode.RESPONSE_OK) {
			List<Goods4Agent> goods=(List<Goods4Agent>)commodityDao.queryGoods4Agent(condition).getData();
			JSONArray categories = new JSONArray();
			JSONArray series = new JSONArray();
			int length=Integer.parseInt(((String)list.get(list.size()-1).get("date")).substring(8, 10));
			Map<String, int[]> goodsMap=new HashMap<>();
			for (int i = 0; i < goods.size(); i++) {
				goodsMap.put(goods.get(i).getName(), new int[length]);
			}
			for (int i = 0; i < length; i++) {
				categories.add(i+1);
			}
			for (int i = 0; i < list.size();i++) {
				Map<String, Object> map=list.get(i);
				int[] data=goodsMap.get((String)map.get("goodsName"));
				data[Integer.parseInt(((String)map.get("date")).substring(8, 10))-1]=Integer.parseInt(map.get("quantity").toString());
				goodsMap.put((String)map.get("goodsName"), data);
			}
			
			for (String key:goodsMap.keySet()) {
				JSONObject temp=new JSONObject();
				JSONArray data = new JSONArray();
				for (int i = 0; i < goodsMap.get(key).length; i++) {
					data.add(goodsMap.get(key)[i]);
				}
				temp.put("name", key);
				temp.put("data", data);
				series.add(temp);
			}
			JSONObject record = new JSONObject();
			record.put("categories", categories);
			record.put("series", series);
			result.setData(record);
		}
		
		return result;
	}

	@Override
	public ResultData purchaseRecordEveryMonth() {
		ResultData result = new ResultData();
		ResultData response =statisticDao.purchaseRecordEveryMonth();
		List<Map<String, Object>> list =(List<Map<String, Object>>) response.getData();
		if (list.size()==0) {
			return result;
		}
		Map<String, Object> condition=new HashMap<>();
		if (commodityDao.queryGoods4Agent(condition).getResponseCode()==ResponseCode.RESPONSE_OK) {
			List<Goods4Agent> goods=(List<Goods4Agent>)commodityDao.queryGoods4Agent(condition).getData();
			JSONArray categories = new JSONArray();
			JSONArray series = new JSONArray();
			int length=Integer.parseInt(((String)list.get(list.size()-1).get("date")).substring(5, 7));
			Map<String, int[]> goodsMap=new HashMap<>();
			for (int i = 0; i < goods.size(); i++) {
				goodsMap.put(goods.get(i).getName(), new int[length]);
			}
			for (int i = 0; i < length; i++) {
				categories.add(i+1);
			}
			for (int i = 0; i < list.size();i++) {
				Map<String, Object> map=list.get(i);
				int[] data=goodsMap.get((String)map.get("goodsName"));
				data[Integer.parseInt(((String)map.get("date")).substring(5, 7))-1]=Integer.parseInt(map.get("quantity").toString());
				goodsMap.put((String)map.get("goodsName"), data);
			}
			
			for (String key:goodsMap.keySet()) {
				JSONObject temp=new JSONObject();
				JSONArray data = new JSONArray();
				for (int i = 0; i < goodsMap.get(key).length; i++) {
					data.add(goodsMap.get(key)[i]);
				}
				temp.put("name", key);
				temp.put("data", data);
				series.add(temp);
			}
			JSONObject record = new JSONObject();
			record.put("categories", categories);
			record.put("series", series);
			result.setData(record);
		}
		
		return result;
	}


}
