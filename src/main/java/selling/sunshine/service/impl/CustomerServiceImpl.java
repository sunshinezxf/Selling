package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.CustomerDao;
import selling.sunshine.model.Agent;
import selling.sunshine.model.Customer;
import selling.sunshine.service.CustomerService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/5/16.
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    private Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerDao customerDao;

    @Override
    public ResultData createCustomer(Customer customer) {
        ResultData result = new ResultData();
        ResultData insertResponse = customerDao.insertCustomer(customer);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData updateCustomer(Customer customer) {
        ResultData result = new ResultData();
        ResultData updateResponse = customerDao.updateCustomer(customer);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

	@Override
	public ResultData fetchCustomer(Agent agent) {
		return null;
	}
    
    
}
