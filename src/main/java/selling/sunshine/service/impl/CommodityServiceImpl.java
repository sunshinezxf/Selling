package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import selling.sunshine.dao.CommodityDao;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.goods.Thumbnail;
import common.sunshine.pagination.DataTableParam;
import selling.sunshine.service.CommodityService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
@Service
public class CommodityServiceImpl implements CommodityService {
	private Logger logger = LoggerFactory.getLogger(CommodityServiceImpl.class);

	@Autowired
	private CommodityDao commodityDao;

	@Override
	public ResultData createGoods4Customer(Goods4Customer goods) {
		ResultData result = new ResultData();
		ResultData response = commodityDao.insertGoods4Customer(goods);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			response.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchGoods4Customer(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = commodityDao.queryGoods4Customer(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List) response.getData()).isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchGoods4Customer(Map<String, Object> condition,
			DataTableParam param) {
		ResultData result = new ResultData();
		ResultData response = commodityDao.queryGoods4CustomerByPage(condition,
				param);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			response.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchGoods4Agent(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = commodityDao.queryGoods4Agent(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List) response.getData()).isEmpty()) {
				response.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(response.getData());
		}
		return result;
	}

	@Override
	public ResultData updateGoods4Customer(Goods4Customer goods) {
		ResultData result = new ResultData();
		ResultData response = commodityDao.updateGoods4Customer(goods);
		response.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData createThumbnail(Thumbnail thumbnail) {
		ResultData result = new ResultData();
		ResultData insertResponse = commodityDao
				.insertGoodsThumbnail(thumbnail);
		result.setResponseCode(insertResponse.getResponseCode());
		if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		} else {
			result.setDescription(insertResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData deleteGoodsThumbnail(String thumbnailId) {
		ResultData result = new ResultData();
		ResultData deleteResponse = commodityDao
				.deleteGoodsThumbnail(thumbnailId);
		result.setResponseCode(deleteResponse.getResponseCode());
		if (deleteResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(deleteResponse.getData());
		} else {
			result.setDescription(deleteResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchThumbnail(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = commodityDao.queryThumbnail(condition);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List) queryResponse.getData()).isEmpty()) {
				queryResponse.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(queryResponse.getData());
		}
		return result;
	}

	@Override
	public ResultData fetchThumbnail() {
		ResultData result = new ResultData();
		ResultData queryResponse = commodityDao.queryThumbnail();
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List) queryResponse.getData()).isEmpty()) {
				queryResponse.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(queryResponse.getData());
		}
		return result;
	}

	@Override
	public ResultData updateThumbnails(List<Thumbnail> thumbnails) {
		ResultData result = new ResultData();
		ResultData response = commodityDao.updateGoodsThumbnail(thumbnails);
		response.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}
}
