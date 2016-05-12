package selling.sunshine.service.impl;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import selling.sunshine.dao.AgentDao;
import selling.sunshine.dao.OrderDao;
import selling.sunshine.dao.OrderItemDao;
import selling.sunshine.model.Order;
import selling.sunshine.model.OrderItem;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

public class OrderServiceImpl implements OrderService {
	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDao orderDao;

	@Override
	public ResultData placeOrder(Order order) {
		ResultData result = new ResultData();
		ResultData insertResponse = orderDao.insertOrder(order);
		result.setResponseCode(insertResponse.getResponseCode());
		if(insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		}else {
			result.setDescription(insertResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchOrder(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return null;
	}

}
