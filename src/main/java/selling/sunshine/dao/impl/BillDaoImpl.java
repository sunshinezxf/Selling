package selling.sunshine.dao.impl;

import java.util.List;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.BillDao;
import selling.sunshine.model.DepositBill;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/10/16.
 */
@Repository
public class BillDaoImpl extends BaseDao implements BillDao {

    private Logger logger = LoggerFactory.getLogger(BillDaoImpl.class);

    private Object lock = new Object();

    @Transactional
    @Override
    public ResultData insertDepositBill(DepositBill bill) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                bill.setBillId(IDGenerator.generate("DPB"));
                sqlSession.insert("selling.bill.deposit.insert", bill);
                result.setData(bill);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

	@Override
	public ResultData queryDepositBill(Map<String, Object> condition) {
		  ResultData result = new ResultData();
	        try {
	            List<DepositBill> list = sqlSession.selectList("selling.bill.deposit.query", condition);
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
