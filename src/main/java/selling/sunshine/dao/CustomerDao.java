package selling.sunshine.dao;

import selling.sunshine.model.Customer;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/5/16.
 */
public interface CustomerDao {
    ResultData insertCustomer(Customer customer);

    ResultData updateCustomer(Customer customer);
    
    ResultData deleteCustomer(Customer customer);

    ResultData queryCustomer(Map<String, Object> condition);

    ResultData queryCustomerByPage(Map<String, Object> condition, DataTableParam param);
}
