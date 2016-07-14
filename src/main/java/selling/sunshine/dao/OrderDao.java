package selling.sunshine.dao;

import selling.sunshine.model.Order;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

public interface OrderDao {
    ResultData insertOrder(Order order);

    ResultData queryOrder(Map<String, Object> condition);

    ResultData queryOrderByPage(Map<String, Object> condition, MobilePageParam param);

    ResultData queryOrderByPage(Map<String, Object> condition, DataTableParam param);

    ResultData updateOrder(Order order);

    ResultData updateOrderLite(Order order);

    ResultData sumOrder();
}
