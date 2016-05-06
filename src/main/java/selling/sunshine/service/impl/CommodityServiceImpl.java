package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.CommodityDao;
import selling.sunshine.model.Goods;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.CommodityService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
@Service
public class CommodityServiceImpl implements CommodityService {
    private Logger logger = LoggerFactory.getLogger(CommodityServiceImpl.class);

    @Autowired
    private CommodityDao commodityDao;

    public ResultData createCommodity(Goods goods) {
        ResultData result = new ResultData();
        ResultData insertResponse = commodityDao.insertCommodity(goods);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchCommodity(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = commodityDao.queryCommodityByPage(condition, param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

	@Override
	public ResultData fetchCommodity(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = commodityDao.queryCommodity(condition);
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(queryResponse.getData());
		} else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}
    
}
