package selling.sunshine.service;

import selling.sunshine.model.Agent;
import selling.sunshine.model.Customer;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/5/16.
 */
public interface CustomerService {
    ResultData createCustomer(Customer customer);

    ResultData updateCustomer(Customer customer);
    
    ResultData fetchCustomer(Agent agent);
}
