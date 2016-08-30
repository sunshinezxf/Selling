package selling.sunshine.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.event.GiftEvent;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.EventDao;
import selling.sunshine.service.EventService;

public class EventServiceImpl implements EventService {

	private Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	private EventDao eventDao;

	@Override
	public ResultData createGiftEvent(GiftEvent event) {
		ResultData result = new ResultData();
		ResultData insertResponse = eventDao.insertGiftEvent(event);
		result.setResponseCode(insertResponse.getResponseCode());
		if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		} else {
			result.setDescription(insertResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchGiftEvent(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = eventDao.queryGiftEvent(condition);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List) queryResponse.getData()).isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(queryResponse.getData());
		} else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData updateGiftEvent(GiftEvent event) {
		ResultData result = new ResultData();
		ResultData updateResponse = eventDao.updateGiftEvent(event);
		result.setResponseCode(updateResponse.getResponseCode());
		if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(updateResponse.getData());
		} else {
			result.setDescription(updateResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchGiftEventByPage(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		ResultData queryResponse = eventDao.queryGiftEventByPage(condition, param);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(queryResponse.getData());
		} else {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
		}
		return result;
	}

	@Override
	public ResultData fetchEventApplication(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = eventDao.queryEventApplication(condition);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List) queryResponse.getData()).isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(queryResponse.getData());
		} else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchEventApplicationByPage(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		ResultData queryResponse = eventDao.queryEventApplication(condition, param);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(queryResponse.getData());
		} else {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
		}
		return result;
	}

	@Override
	public ResultData updateEventApplication(EventApplication eventApplication) {
		ResultData result = new ResultData();
		ResultData updateResponse = eventDao.updateEventApplication(eventApplication);
		result.setResponseCode(updateResponse.getResponseCode());
		if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(updateResponse.getData());
		} else {
			result.setDescription(updateResponse.getDescription());
		}
		return result;
	}

}
