package selling.sunshine.dao;

import selling.sunshine.model.Agent;
import selling.sunshine.model.Customer;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/5/16.
 */
public interface CustomerDao {
    ResultData insertCustomer(Customer customer, Agent agent);

    ResultData updateCustomer(Customer customer, Agent agent);
}
