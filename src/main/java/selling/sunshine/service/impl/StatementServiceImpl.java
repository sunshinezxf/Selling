package selling.sunshine.service.impl;



import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.StatementDao;
import selling.sunshine.model.bill.BillVolume;
import selling.sunshine.service.StatementService;

public class StatementServiceImpl implements StatementService {
	
    private Logger logger = LoggerFactory.getLogger(StatementServiceImpl.class);
    
    @Autowired
    private StatementDao statementDao;

	@Override
	public ResultData payedBillStatement(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = statementDao.payedBillStatement(condition);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List<BillVolume>) queryResponse.getData()).isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(queryResponse.getData());
		}
		return result;
	}

	@Override
	public ResultData refundBillStatement(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = statementDao.refundBillStatement(condition);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List<BillVolume>) queryResponse.getData()).isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
			result.setData(queryResponse.getData());
		}
		return result;
	}


    



}
