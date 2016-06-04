package selling.sunshine.dao.impl;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.OrderItemDao;
import selling.sunshine.model.OrderItem;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

@Repository
public class OrderItemDaoImpl extends BaseDao implements OrderItemDao {
    private Logger logger = LoggerFactory.getLogger(OrderItemDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertOrderItems(List<OrderItem> orderItems) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                for (OrderItem orderItem : orderItems) {
                    orderItem.setOrderItemId(IDGenerator.generate("ODR"));
                }
                sqlSession.insert("selling.order.insertBatch", orderItems);
                result.setData(orderItems);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result = insertOrderItems(orderItems);
            } finally {
                return result;
            }
        }
    }

    @Override
    public ResultData queryOrderItem(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<OrderItem> list = sqlSession.selectList("selling.order.item.query", condition);
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
