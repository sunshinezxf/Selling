package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.CustomerDao;
import selling.sunshine.model.Customer;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.CustomerService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.Map;

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
    public ResultData deleteCustomer(Customer customer) {
        ResultData result = new ResultData();
        ResultData deleteResponse = customerDao.deleteCustomer(customer);
        result.setResponseCode(deleteResponse.getResponseCode());
        if (deleteResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(deleteResponse.getData());
        } else {
            result.setDescription(deleteResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchCustomer(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = customerDao.queryCustomer(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchCustomer(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = customerDao.queryCustomerByPage(condition, param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchCustomerPhone(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = customerDao.queryCustomerPhone(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchCustomerAddress(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = customerDao.queryCustomerAddress(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }


}
