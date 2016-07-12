package selling.sunshine.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import selling.sunshine.dao.StatisticDao;
import selling.sunshine.model.sum.Sum4Order;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.StatisticService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.*;

/**
 * Created by sunshine on 6/24/16.
 */
@Service
public class StatisticServiceImpl implements StatisticService {
    private Logger logger = LoggerFactory.getLogger(StatisticServiceImpl.class);

    @Autowired
    private StatisticDao statisticDao;

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
                    if(sum.size() >= index + 1) {
                    	Sum4Order current = sum.get(index);
                    	if (current.getQuantity() == temp) {
                    		val.add(current.getNum());
                    		index++;
                    	}else {
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
		ResultData queryResponse=statisticDao.orderStatisticsByPage(param);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(queryResponse.getData());
		} else {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}
}
