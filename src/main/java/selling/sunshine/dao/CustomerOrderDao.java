package selling.sunshine.dao;

import java.util.Map;

import selling.sunshine.model.CustomerOrder;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 6/14/16.
 */
public interface CustomerOrderDao {
    ResultData insertOrder(CustomerOrder order);
    
    ResultData updateOrder(CustomerOrder order);
    
    ResultData queryOrder(Map<String, Object> condition);
}
