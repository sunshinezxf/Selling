package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.CustomerDao;
import selling.sunshine.model.Customer;
import selling.sunshine.model.CustomerAddress;
import selling.sunshine.model.CustomerPhone;
import selling.sunshine.model.Goods;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/5/16.
 */
@Repository
public class CustomerDaoImpl extends BaseDao implements CustomerDao {

    private Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);
    private Object lock = new Object();

    @Transactional
    @Override
    public ResultData insertCustomer(Customer customer) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                customer.setCustomerId(IDGenerator.generate("CUS"));
                sqlSession.insert("selling.customer.insert", customer);
                CustomerPhone phoneNumber = customer.getPhone();
                CustomerAddress address = customer.getAddress();
                phoneNumber.setPhoneId(IDGenerator.generate("PNM"));
                address.setAddressId(IDGenerator.generate("ADD"));
                sqlSession.insert("selling.customer.phone.insert", phoneNumber);
                sqlSession.insert("selling.customer.address.insert", address);
                result.setData(customer);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result = insertCustomer(customer);
            } finally {
                return result;
            }
        }
    }

    @Transactional
    @Override
    public ResultData updateCustomer(Customer customer) {
        ResultData result = new ResultData();
        try {

            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("customerId", customer.getCustomerId());
            condition.put("name", customer.getName());
            condition.put("blockFlag", customer.isBlockFlag());

            if (ResponseCode.RESPONSE_OK.equals(queryCustomer(condition).getResponseCode())) {
                CustomerPhone phoneNumber = customer.getPhone();
                CustomerAddress address = customer.getAddress();

                sqlSession.insert("selling.customer.phone.insert", phoneNumber);
                sqlSession.insert("selling.customer.address.insert", address);
                Customer c = ((List<Customer>) queryCustomer(condition).getData()).get(0);

                sqlSession.update("selling.customer.phone.block", c.getPhone());
                sqlSession.update("selling.customer.address.block", c.getAddress());
            }
            result.setData(customer);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        } finally {
            return result;
        }

    }

    @Override
    public ResultData queryCustomer(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Goods> list = sqlSession.selectList("selling.customer.query", condition);
            result.setData(list);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }
}
