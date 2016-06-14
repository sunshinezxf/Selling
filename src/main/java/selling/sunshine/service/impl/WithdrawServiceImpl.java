package selling.sunshine.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import selling.sunshine.dao.WithdrawDao;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.WithdrawService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

public class WithdrawServiceImpl implements WithdrawService {

	@Autowired
	private WithdrawDao withdrawDao;

	@Override
	public ResultData fetchWithdrawRecord(Map<String, Object> condition,
			DataTableParam param) {
		ResultData result = new ResultData();
		ResultData queryResponse = withdrawDao.queryWithdrawByPage(condition,
				param);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(queryResponse.getData());
		} else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchWithdrawRecord(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = withdrawDao.queryWithdraw(condition);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(queryResponse.getData());
		} else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}

}
