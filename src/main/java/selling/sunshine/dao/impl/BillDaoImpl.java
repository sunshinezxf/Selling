package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.BillDao;
import selling.sunshine.model.BillStatus;
import selling.sunshine.model.DepositBill;
import selling.sunshine.model.OrderBill;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
    @Override
    public ResultData updateDepositBill(DepositBill bill) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.bill.deposit.update", bill);
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
    public ResultData queryOrderBill(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<OrderBill> list = sqlSession.selectList("selling.bill.order.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Transactional
    @Override
    public ResultData insertOrderBill(OrderBill bill) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                bill.setBillId(IDGenerator.generate("ODB"));
                if (bill.getChannel().equals("coffer")) {
                    bill.setStatus(BillStatus.PAYED);
                }
                sqlSession.insert("selling.bill.order.insert", bill);
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

    @Transactional
    @Override
    public ResultData updateOrderBill(OrderBill bill) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.bill.order.update", bill);
                Map<String, Object> condition = new HashMap<>();
                condition.put("billId", bill.getBillId());
                bill = sqlSession.selectOne("selling.bill.order.query", condition);
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
}
