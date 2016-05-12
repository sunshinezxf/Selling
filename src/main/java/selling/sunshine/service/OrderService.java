package selling.sunshine.service;

import java.util.Map;

import selling.sunshine.model.Order;
import selling.sunshine.utils.ResultData;

public interface OrderService {
	ResultData placeOrder(Order order);
	
	ResultData fetchOrder(Map<String, Object> condition);
}