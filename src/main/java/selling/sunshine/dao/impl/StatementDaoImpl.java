package selling.sunshine.dao.impl;

import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.sunshine.dao.BaseDao;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.StatementDao;
import selling.sunshine.model.bill.BillVolume;

public class StatementDaoImpl extends BaseDao implements StatementDao {
	
    private Logger logger = LoggerFactory.getLogger(StatementDaoImpl.class);

	@Override
	public ResultData payedBillStatement(Map<String, Object> condition) {
		ResultData result = new ResultData();
        try {
            List<BillVolume> list = sqlSession.selectList("selling.bill.sum.queryPayedBill", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
	}

	@Override
	public ResultData refundBillStatement(Map<String, Object> condition) {
		ResultData result = new ResultData();
        try {
            List<BillVolume> list = sqlSession.selectList("selling.bill.sum.queryRefundBill", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
	}


}
