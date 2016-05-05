package selling.sunshine.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.OrderDao;
import selling.sunshine.model.Order;
import selling.sunshine.model.OrderItem;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

public class OrderDaoImpl extends BaseDao implements OrderDao {
	private Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

	private Object lock = new Object();
	
	@Transactional
	@Override
	public ResultData insertOrder(Order order) {
		List<OrderItem> orderItems = order.getOrderItems();
		 ResultData result = new ResultData();
	        synchronized (lock) {
	            try {
	                order.setOrderId(IDGenerator.generate("ODR"));
	                sqlSession.insert("selling.order.insert", order);
	                for(OrderItem orderItem : orderItems) {
	            		orderItem.setOrderItemId(IDGenerator.generate("ODR"));
	            		orderItem.setOrder(order);
	            	}
	                sqlSession.insert("selling.order.item.insertBatch", orderItems);
	                order.setOrderItems(orderItems);
	                result.setData(order);
	            } catch (Exception e) {
	                logger.error(e.getMessage());
	                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	                result = insertOrder(order);
	            } finally {
	                return result;
	            }
	        }
	}

}
