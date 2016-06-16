package selling.sunshine.service;

import java.util.Map;

import selling.sunshine.model.CustomerOrder;
import selling.sunshine.model.Order;
import selling.sunshine.model.OrderItem;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.utils.ResultData;

public interface OrderService {
	ResultData placeOrder(Order order);
	
	ResultData placeOrder(CustomerOrder customerOrder);
	
	ResultData payOrder(Order order);
	
	ResultData payOrder(CustomerOrder customerOrder);
	
	ResultData modifyOrder(Order order);
	
	ResultData fetchOrder(Map<String, Object> condition);
	
	ResultData fetchCustomerOrder(Map<String, Object> condition);
	
	ResultData fetchOrder(Map<String, Object> condition, MobilePageParam param);
	
	ResultData fetchOrderPool(Map<String, Object> condition);

	ResultData poolOrder();
	
	ResultData cancel(Order order);
	
	ResultData fetchOrderItem(Map<String, Object> condition);
	
	ResultData updateOrderItem(OrderItem orderItem);
}
