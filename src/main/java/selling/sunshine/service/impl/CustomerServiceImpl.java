package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import selling.sunshine.model.Agent;
import selling.sunshine.model.Customer;
import selling.sunshine.service.CustomerService;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/5/16.
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    private Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public ResultData addCustomer(Customer customer, Agent agent) {
        ResultData result = new ResultData();

        return result;
    }

    @Override
    public ResultData updateCustomer(Customer customer, Agent agent) {
        ResultData result = new ResultData();

        return result;
    }
}
