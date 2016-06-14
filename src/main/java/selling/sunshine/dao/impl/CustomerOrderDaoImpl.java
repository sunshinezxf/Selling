package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.CustomerOrderDao;
import selling.sunshine.model.CustomerOrder;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 6/14/16.
 */
@Repository
public class CustomerOrderDaoImpl extends BaseDao implements CustomerOrderDao {
    private Logger logger = LoggerFactory.getLogger(CustomerOrderDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertOrder(CustomerOrder order) {
        ResultData result = new ResultData();
        order.setOrderId(IDGenerator.generate("CUO"));
        synchronized (lock) {
            try {
                sqlSession.insert("selling.customer.order.insert", order);
                result.setData(order);
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
