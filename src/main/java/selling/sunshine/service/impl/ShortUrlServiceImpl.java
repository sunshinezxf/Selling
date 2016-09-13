package selling.sunshine.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import common.sunshine.model.selling.util.ShortUrl;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.ShortUrlDao;
import selling.sunshine.service.ShortUrlService;

public class ShortUrlServiceImpl implements ShortUrlService {
	private Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	private ShortUrlDao shortUrlDao;
	
	@Override
	public ResultData createShortUrl(ShortUrl shortUrl) {
		ResultData result = new ResultData();
		ResultData insertResponse = shortUrlDao.insertShortUrl(shortUrl);
		result.setResponseCode(insertResponse.getResponseCode());
		if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		} else {
			result.setDescription(insertResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchShortUrl(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = shortUrlDao.queryShortUrl(condition);
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
