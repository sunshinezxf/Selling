package selling.sunshine.service.impl;

import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.customer.CustomerAddress;
import common.sunshine.model.selling.customer.CustomerPhone;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.AgentDao;
import selling.sunshine.dao.CustomerDao;
import selling.sunshine.dao.PurchaseDao;
import selling.sunshine.service.CustomerService;
import selling.sunshine.vo.customer.CustomerVo;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/5/16.
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    private Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private AgentDao agentDao;

    @Autowired
    private PurchaseDao purchaseDao;

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
            if (((List<CustomerVo>) queryResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
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
            if (((List<CustomerPhone>) queryResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
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

    @Override
    public ResultData updateCustomerAddress(CustomerAddress customerAddress) {
        ResultData result = new ResultData();
        ResultData updateResponse = customerDao.updateCustomerAddress(customerAddress);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData customerTransform(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = customerDao.queryCustomer(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CustomerVo> list = (List<CustomerVo>) response.getData();
            for (CustomerVo item : list) {
                condition.put("phone", item.getPhone());
                result = agentDao.queryAgent(condition);
                if (((List<Agent>) result.getData()).size() != 0) {
                    Customer customer = new Customer(item.getName(), item.getAddress(), item.getPhone());
                    customer.setCustomerId(item.getCustomerId());
                    customerDao.updateCustomer(customer);
                }
            }
        }
        return result;
    }

    @Override
    public ResultData fetchCustomerPurchase(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData response = purchaseDao.queryCustomerPurchaseByPage(condition, param);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData fetchCustomerPurchase(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = purchaseDao.queryCustomerPurchase(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        } else {
            result.setData(response.getData());
        }
        return result;
    }
}
