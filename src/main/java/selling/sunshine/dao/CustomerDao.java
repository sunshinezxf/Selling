package selling.sunshine.dao;

import java.util.Map;

import selling.sunshine.model.Agent;
import selling.sunshine.model.Customer;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/5/16.
 */
public interface CustomerDao {
    ResultData insertCustomer(Customer customer);

    ResultData updateCustomer(Customer customer);
    
    ResultData queryCustomer(Map<String, Object> condition);
}
