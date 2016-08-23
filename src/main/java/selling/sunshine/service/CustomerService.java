package selling.sunshine.service;

import common.sunshine.model.selling.customer.Customer;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/5/16.
 */
public interface CustomerService {
    ResultData createCustomer(Customer customer);

    ResultData updateCustomer(Customer customer);
    
    ResultData deleteCustomer(Customer customer);

    ResultData fetchCustomer(Map<String, Object> condition);

    ResultData fetchCustomer(Map<String, Object> condition, DataTableParam param);
    
    ResultData fetchCustomerPhone(Map<String, Object> condition);
    
    ResultData fetchCustomerAddress(Map<String, Object> condition);
}
