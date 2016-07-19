package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import selling.sunshine.dao.RefundDao;
import selling.sunshine.model.RefundConfig;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.RefundService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/17/16.
 */
@Service
public class RefundServiceImpl implements RefundService {

	private Logger logger = LoggerFactory.getLogger(RefundServiceImpl.class);

	@Autowired
	private RefundDao refundDao;

	@Override
	public ResultData createRefundConfig(RefundConfig config) {
		ResultData result = new ResultData();
		ResultData insertResponse = refundDao.insertRefundConfig(config);
		result.setResponseCode(insertResponse.getResponseCode());
		if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		} else {
			result.setDescription(insertResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchRefundConfig(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = refundDao.queryRefundConfig(condition);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List<RefundConfig>) queryResponse.getData()).size() == 0) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			} else {
				result.setData(queryResponse.getData());
			}
		} else {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData refundRecord() {
		ResultData result = new ResultData();
		ResultData refundResponse = refundDao.refundRecord();
		result.setResponseCode(refundResponse.getResponseCode());
		if (refundResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(refundResponse.getData());
		} else {
			result.setDescription(refundResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchRefundRecord(Map<String, Object> condition) {
		ResultData resultData = new ResultData();
		ResultData queryResponse = refundDao.queryRefundRecord(condition);
		resultData.setResponseCode(queryResponse.getResponseCode());
		if(queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
			resultData.setData(queryResponse.getData());
		} else {
			resultData.setDescription(queryResponse.getDescription());
		}
		return resultData;
	}

	@Override
	public ResultData fetchRefundRecordByPage(Map<String, Object> condition,
			DataTableParam param) {
		ResultData resultData = new ResultData();
		ResultData queryResponse = refundDao.queryRefundRecordByPage(condition,param);
		resultData.setResponseCode(queryResponse.getResponseCode());
		if(queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
			resultData.setData(queryResponse.getData());
		} else {
			resultData.setDescription(queryResponse.getDescription());
		}
		return resultData;
	}

	@Override
	public ResultData refund() {
		ResultData resultData = new ResultData();
		resultData=refundDao.refund();
		return resultData;
	}

	@Override
	public ResultData statistic(Map<String, Object> condition) {
		ResultData result = new ResultData();
		result=refundDao.statistic(condition);
		if (result.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List) result.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
		}
		return result;
	}

	@Override
	public ResultData calculateRefund(String agentId) {
		ResultData result = new ResultData();
		result = refundDao.calculateRefund(agentId);
		if (result.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List) result.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
		}
		return result;
	}

	@Override
	public ResultData calculateQuantity(String agentId) {
		//ResultData Code仅在agentId不存在时为error
		//ResultData data为一个Integer，记录该agent当月买了多少件商品(和Refund的quantity一样，但是是所有商品累计的，赠送不算，客户下的算，直接下的算)
		return refundDao.calculateQuantity(agentId);
	}
	
	@Override
	public ResultData calculateQuantityAll(String agentId){
		//ResultData Code仅在agentId不存在时为error
		//ResultData data为三个Integer的list，记录该agent本日当月和累计买了多少件商品(和Refund的quantity一样，但是是所有商品累计的，赠送不算，客户下的算，直接下的算)
		return refundDao.calculateQuantityAll(agentId);
	}
	
	
}
