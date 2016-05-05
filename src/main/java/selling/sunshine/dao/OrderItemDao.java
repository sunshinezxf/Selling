package selling.sunshine.dao;

import java.util.List;

import selling.sunshine.model.OrderItem;
import selling.sunshine.utils.ResultData;

public interface OrderItemDao {
	public ResultData insertOrderItems(List<OrderItem> orderItems);
}
