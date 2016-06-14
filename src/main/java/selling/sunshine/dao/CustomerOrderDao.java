package selling.sunshine.dao;

import selling.sunshine.model.CustomerOrder;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 6/14/16.
 */
public interface CustomerOrderDao {
    ResultData insertOrder(CustomerOrder order);
}
