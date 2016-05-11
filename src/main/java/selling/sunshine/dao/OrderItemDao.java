package selling.sunshine.dao;

import selling.sunshine.model.OrderItem;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

public interface OrderItemDao {
    ResultData insertOrderItems(List<OrderItem> orderItems);

    ResultData queryOrderItem(Map<String, Object> condition);
}
