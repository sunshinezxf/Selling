package selling.sunshine.service;

import selling.sunshine.model.Customer;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/5/16.
 */
public interface CustomerService {
    ResultData createCustomer(Customer customer);

    ResultData updateCustomer(Customer customer);

    ResultData fetchCustomer(Map<String, Object> condition);

    ResultData fetchCustomer(Map<String, Object> condition, DataTableParam param);
}
