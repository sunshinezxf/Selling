package selling.sunshine.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import selling.sunshine.dao.BackOperationLogDao;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.service.LogService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

public class LogServiceImpl implements LogService {

	@Autowired
	private BackOperationLogDao backOperationLogDao;
	
	@Override
	public ResultData fetchBackOperationLog(Map<String, Object> condition) {
		 ResultData result = new ResultData();
	        ResultData queryResponse = backOperationLogDao.queryBackOperationLog(condition);
	        result.setResponseCode(queryResponse.getResponseCode());
	        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
	            if (((List<BackOperationLog>) queryResponse.getData()).isEmpty()) {
	                result.setResponseCode(ResponseCode.RESPONSE_NULL);
	            }
	            result.setData(queryResponse.getData());
	        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
	            result.setDescription(queryResponse.getDescription());
	        }
	        return result;
	}

	@Override
	public ResultData createbackOperationLog(BackOperationLog backOperationLog) {
		 ResultData result = new ResultData();
	        ResultData insertResponse = backOperationLogDao.insertBackOperationLog(backOperationLog);
	        result.setResponseCode(insertResponse.getResponseCode());
	        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
	            result.setData(insertResponse.getData());
	        } else {
	            result.setDescription(insertResponse.getDescription());
	        }
	        return result;
	}

}
