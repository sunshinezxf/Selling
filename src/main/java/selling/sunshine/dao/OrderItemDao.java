package selling.sunshine.dao;

import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

public interface OrderItemDao {
    ResultData insertOrderItems(List<OrderItem> orderItems);
    
    ResultData queryOrderItem(Map<String, Object> condition);

    ResultData queryOrderItemByPage(Map<String, Object> condition, DataTableParam param);

    ResultData queryOrderItemSum(Map<String, Object> condition);

    ResultData queryOrderItemSum(Map<String, Object> condition, DataTableParam param);
    
    ResultData updateOrderItem(OrderItem orderItem);
}
