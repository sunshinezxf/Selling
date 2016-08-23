package selling.sunshine.dao.impl;

import common.sunshine.utils.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import common.sunshine.dao.BaseDao;
import selling.sunshine.dao.BillDao;
import common.sunshine.model.selling.bill.support.BillStatus;
import common.sunshine.model.selling.bill.CustomerOrderBill;
import common.sunshine.model.selling.bill.DepositBill;
import common.sunshine.model.selling.bill.OrderBill;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账号与持久层交互接口,包括充值账单和订单支付账单
 * Created by sunshine on 5/10/16.
 */
@Repository
public class BillDaoImpl extends BaseDao implements BillDao {
    private Logger logger = LoggerFactory.getLogger(BillDaoImpl.class);

    private Object lock = new Object();

    /**
     * 添加充值账单记录
     *
     * @param bill
     * @return
     */
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

    /**
     * 查询代理商的充值账单记录
     *
     * @param condition
     * @return
     */
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

    /**
     * 更新充值账单记录
     *
     * @param bill
     * @return
     */
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

    /**
     * 查询订单支付账单记录
     *
     * @param condition
     * @return
     */
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

    /**
     * 插入订单支付账单记录,如果是使用账户余额付款,则订单的支付状态直接修改为已付款
     *
     * @param bill
     * @return
     */
    @Transactional
    @Override
    public ResultData insertOrderBill(OrderBill bill) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                bill.setBillId(IDGenerator.generate("ODB"));
                if (!StringUtils.isEmpty(bill.getChannel()) && bill.getChannel().equals("coffer")) {
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

    /**
     * 更新订单账单信息
     *
     * @param bill
     * @return
     */
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

    @Override
    public ResultData insertCustomerOrderBill(CustomerOrderBill bill) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                bill.setBillId(IDGenerator.generate("COB"));
                sqlSession.insert("selling.bill.customerOrder.insert", bill);
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
    public ResultData queryCustomerOrderBill(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<OrderBill> list = sqlSession.selectList("selling.bill.customerOrder.query", condition);
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
    public ResultData updateCustomerOrderBill(CustomerOrderBill bill) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.bill.customerOrder.update", bill);
                Map<String, Object> condition = new HashMap<>();
                condition.put("billId", bill.getBillId());
                bill = sqlSession.selectOne("selling.bill.customerOrder.query", condition);
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
