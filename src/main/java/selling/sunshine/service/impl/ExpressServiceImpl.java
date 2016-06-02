package selling.sunshine.service.impl;

import java.util.List;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import selling.sunshine.dao.ExpressDao;
import selling.sunshine.model.Express;
import selling.sunshine.service.ExpressService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

public class ExpressServiceImpl implements ExpressService {
	
	private Logger logger = LoggerFactory.getLogger(ExpressServiceImpl.class);

	@Autowired
	private ExpressDao expressDao;

	@Override
	public ResultData createExpress(Express express) {
		ResultData result = new ResultData();
		ResultData insertResponse = expressDao.insertExpress(express);
		result.setResponseCode(insertResponse.getResponseCode());
		if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		} else {
			result.setDescription(insertResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchExpress(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = expressDao.queryExpress(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Express>) queryResponse.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
	}

}
