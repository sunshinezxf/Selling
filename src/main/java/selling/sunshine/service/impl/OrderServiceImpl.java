package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.CustomerOrderDao;
import selling.sunshine.dao.OrderDao;
import selling.sunshine.dao.OrderItemDao;
import selling.sunshine.dao.OrderPoolDao;
import selling.sunshine.model.CustomerOrder;
import selling.sunshine.model.Order;
import selling.sunshine.model.OrderItem;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderPoolDao orderPoolDao;

	@Autowired
	private OrderItemDao orderItemDao;

	@Autowired
	private CustomerOrderDao customerOrderDao;

	@Override
	public ResultData placeOrder(Order order) {
		ResultData result = new ResultData();
		ResultData insertResponse = orderDao.insertOrder(order);
		result.setResponseCode(insertResponse.getResponseCode());
		if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		} else {
			result.setDescription(insertResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData placeOrder(CustomerOrder customerOrder) {
		ResultData result = new ResultData();
		ResultData insertResponse = customerOrderDao.insertOrder(customerOrder);
		result.setResponseCode(insertResponse.getResponseCode());
		if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		} else {
			result.setDescription(insertResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData payOrder(Order order) {
		ResultData result = new ResultData();
		ResultData updateResponse = orderDao.updateOrder(order);
		result.setResponseCode(updateResponse.getResponseCode());
		if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(updateResponse.getData());
		} else {
			result.setDescription(updateResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData payOrder(CustomerOrder customerOrder) {
		ResultData result = new ResultData();
		ResultData updateResponse = customerOrderDao.updateOrder(customerOrder);
		result.setResponseCode(updateResponse.getResponseCode());
		if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(updateResponse.getData());
		} else {
			result.setDescription(updateResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchOrder(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = orderDao.queryOrder(condition);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List<Order>) queryResponse.getData()).isEmpty()) {
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
	public ResultData fetchCustomerOrder(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = customerOrderDao.queryOrder(condition);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List<CustomerOrder>) queryResponse.getData()).isEmpty()) {
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
	public ResultData fetchCustomerOrder(Map<String, Object> condition,
			MobilePageParam param) {
		ResultData result = new ResultData();
		ResultData queryResponse = customerOrderDao.queryOrderByPage(condition,
				param);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(queryResponse.getData());
		} else {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchCustomerOrder(Map<String, Object> condition,
			DataTableParam param) {
		ResultData result = new ResultData();
		ResultData queryResponse = customerOrderDao.queryOrderByPage(condition,
				param);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(queryResponse.getData());
		} else {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData modifyOrder(Order order) {
		ResultData result = new ResultData();
		ResultData updateResponse = orderDao.updateOrder(order);
		result.setResponseCode(updateResponse.getResponseCode());
		if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(updateResponse.getData());
		} else {
			result.setDescription(updateResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData poolOrder() {
		ResultData result = new ResultData();
		ResultData sumResponse = orderDao.sumOrder();
		result.setResponseCode(sumResponse.getResponseCode());
		if (sumResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(sumResponse.getData());
		} else {
			result.setDescription(sumResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchOrderPool(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData fetchResponse = orderPoolDao.queryOrderPool(condition);
		result.setResponseCode(fetchResponse.getResponseCode());
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(fetchResponse.getData());
		} else {
			result.setDescription(fetchResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData cancel(Order order) {
		ResultData result = new ResultData();
		ResultData cancelResponse = orderDao.cancelOrder(order);
		result.setResponseCode(cancelResponse.getResponseCode());
		if (cancelResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(cancelResponse.getData());
		} else {
			result.setDescription(cancelResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchOrder(Map<String, Object> condition,
			MobilePageParam param) {
		ResultData result = new ResultData();
		ResultData queryResponse = orderDao.queryOrderByPage(condition, param);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(queryResponse.getData());
		} else {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchOrder(Map<String, Object> condition,
			DataTableParam param) {
		ResultData result = new ResultData();
		ResultData queryResponse = orderDao.queryOrderByPage(condition, param);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(queryResponse.getData());
		} else {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchOrderItem(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData fetchResponse = orderItemDao.queryOrderItem(condition);
		result.setResponseCode(fetchResponse.getResponseCode());
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(fetchResponse.getData());
		} else {
			result.setDescription(fetchResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData updateOrderItem(OrderItem orderItem) {
		ResultData result = new ResultData();
		ResultData updateResponse = orderItemDao.updateOrderItem(orderItem);
		result.setResponseCode(updateResponse.getResponseCode());
		if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(updateResponse.getData());
		} else {
			result.setDescription(updateResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData updateCustomerOrder(CustomerOrder customerOrder) {
		ResultData result = new ResultData();
		ResultData updateResponse = customerOrderDao.updateOrder(customerOrder);
		result.setResponseCode(updateResponse.getResponseCode());
		if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(updateResponse.getData());
		} else {
			result.setDescription(updateResponse.getDescription());
		}
		return result;
	}
}
