package selling.sunshine.service;

import java.util.Map;

import selling.sunshine.model.Order;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.utils.ResultData;

public interface OrderService {
	ResultData placeOrder(Order order);
	
	ResultData payOrder(Order order);
	
	ResultData modifyOrder(Order order);
	
	ResultData fetchOrder(Map<String, Object> condition);

	ResultData fetchOrder(Map<String, Object> condition, MobilePageParam param);

	ResultData poolOrder();
	
	ResultData cancel(Order order);
}
