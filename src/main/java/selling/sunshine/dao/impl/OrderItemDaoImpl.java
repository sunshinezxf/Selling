package selling.sunshine.dao.impl;

import common.sunshine.utils.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import common.sunshine.dao.BaseDao;
import selling.sunshine.dao.OrderItemDao;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

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

    @Override
    public ResultData updateOrderItem(OrderItem orderItem) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.order.item.update", orderItem);
                result.setData(orderItem);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                return result;
            }
        }
    }
}
