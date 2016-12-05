package selling.sunshine.service;

import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.pagination.MobilePageParam;
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
    
    ResultData received(Order order);

    ResultData fetchOrderItem(Map<String, Object> condition);

    ResultData fetchOrderItemSum(Map<String, Object> condition);

    ResultData fetchOrderItemSum(Map<String, Object> condition, DataTableParam param);

    ResultData updateOrderItem(OrderItem orderItem);

    ResultData updateCustomerOrder(CustomerOrder customerOrder);

    ResultData check();

    ResultData checkOrderPool(Map<String, Object> condition);
}
