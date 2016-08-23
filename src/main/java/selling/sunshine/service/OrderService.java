package selling.sunshine.service;

import selling.sunshine.model.CustomerOrder;
import selling.sunshine.model.Order;
import selling.sunshine.model.OrderItem;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.pagination.MobilePageParam;
import common.sunshine.utils.ResultData;

import java.util.Map;

public interface OrderService {
    ResultData placeOrder(Order order);

    ResultData placeOrder(CustomerOrder customerOrder);

    ResultData payOrder(Order order);

    ResultData payOrder(CustomerOrder customerOrder);

    ResultData modifyOrder(Order order);

    ResultData fetchOrder(Map<String, Object> condition);

    ResultData fetchCustomerOrder(Map<String, Object> condition);

    ResultData fetchCustomerOrder(Map<String, Object> condition, MobilePageParam param);

    ResultData fetchCustomerOrder(Map<String, Object> condition, DataTableParam param);

    ResultData fetchOrder(Map<String, Object> condition, MobilePageParam param);

    ResultData fetchOrder(Map<String, Object> condition, DataTableParam param);

    ResultData fetchOrderPool(Map<String, Object> condition);

    ResultData poolOrder();

    ResultData cancel(Order order);

    ResultData fetchOrderItem(Map<String, Object> condition);

    ResultData updateOrderItem(OrderItem orderItem);
    
    ResultData updateCustomerOrder(CustomerOrder customerOrder);
}
