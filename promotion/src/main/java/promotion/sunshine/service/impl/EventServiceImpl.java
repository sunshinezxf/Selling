package promotion.sunshine.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import promotion.sunshine.dao.EventDao;
import promotion.sunshine.service.EventService;

public class EventServiceImpl implements EventService {

	@Autowired
	private EventDao eventDao;
	
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
	public ResultData fetchQuestionOption(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = eventDao.queryQuestionOption(condition);
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
	public ResultData insertEventApplication(EventApplication eventApplication) {
		ResultData result = new ResultData();
		ResultData insertResponse = eventDao.insertEventApplication(eventApplication);
		result.setResponseCode(insertResponse.getResponseCode());
		if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		} else {
			result.setDescription(insertResponse.getDescription());
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

}
