package selling.sunshine.dao;

import selling.sunshine.model.Order;
import selling.sunshine.utils.ResultData;

public interface OrderDao {
	public ResultData insertOrder(Order order);

}
