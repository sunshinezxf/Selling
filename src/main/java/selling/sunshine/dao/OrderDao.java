package selling.sunshine.dao;

import common.sunshine.model.selling.order.Order;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.pagination.MobilePageParam;
import common.sunshine.utils.ResultData;

import java.util.Map;

public interface OrderDao {
    ResultData insertOrder(Order order);
    
    ResultData insertOrderLite(Order order);

    ResultData queryOrder(Map<String, Object> condition);

    ResultData queryOrderByPage(Map<String, Object> condition, MobilePageParam param);

    ResultData queryOrderByPage(Map<String, Object> condition, DataTableParam param);

    ResultData updateOrder(Order order);

    ResultData updateOrderLite(Order order);

    ResultData sumOrder();
}
