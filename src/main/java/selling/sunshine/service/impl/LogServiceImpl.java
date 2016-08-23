package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.dao.BackOperationLogDao;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.service.LogService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

public class LogServiceImpl implements LogService {
    private Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

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
    public ResultData fetchBackOperationLog(Map<String, Object> condition, MobilePageParam param) {
        ResultData result = new ResultData();
        ResultData response = backOperationLogDao.queryBackOperationLog(condition, param);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
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
