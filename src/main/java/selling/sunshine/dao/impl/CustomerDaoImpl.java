package selling.sunshine.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.CustomerDao;
import selling.sunshine.model.Customer;
import selling.sunshine.model.CustomerAddress;
import selling.sunshine.model.CustomerPhone;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代理商顾客与持久层交互实现类
 * Created by sunshine on 5/5/16.
 */
@Repository
public class CustomerDaoImpl extends BaseDao implements CustomerDao {
    private Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);

    private Object lock = new Object();

    /**
     * 添加顾客信息记录,涉及customer, customer_phone, customer_address
     *
     * @param customer
     * @return
     */
    @Transactional
    @Override
    public ResultData insertCustomer(Customer customer) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                //在customer表中添加记录
                customer.setCustomerId(IDGenerator.generate("CUS"));
                sqlSession.insert("selling.customer.insert", customer);
                //获取客户的电话并在customer_phone表中添加记录
                CustomerPhone phone = customer.getPhone();
                phone.setPhoneId(IDGenerator.generate("PNM"));
                phone.setCustomer(customer);
                sqlSession.insert("selling.customer.phone.insert", phone);
                //获取客户的地址并在customer_address表中添加记录
                CustomerAddress address = customer.getAddress();
                address.setAddressId(IDGenerator.generate("ADR"));
                address.setCustomer(customer);
                sqlSession.insert("selling.customer.address.insert", address);
                sqlSession.commit();
                //从数据库中获取刚插入的记录
                Map<String, Object> condition = new HashMap<>();
                condition.put("customerId", customer.getCustomerId());
                customer = sqlSession.selectOne("selling.customer.query", condition);
                result.setData(customer);
            } catch (Exception e) {
                sqlSession.rollback();
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    /**
     * 更新顾客信息
     *
     * @param customer
     * @return
     */
    @Transactional
    @Override
    public ResultData updateCustomer(Customer customer) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                Map<String, Object> condition = new HashMap<>();
                condition.put("customerId", customer.getCustomerId());
                condition.put("blockFlag", false);
                //如果新的电话号码与数据库中最新使用的号码不同,则弃用原来的号码并新增一条记录
                CustomerPhone phone = customer.getPhone();
                CustomerPhone currentPhone = sqlSession.selectOne("selling.customer.phone.query", condition);
                if (phone != null || (currentPhone != null && !phone.getPhone().equals(currentPhone.getPhone()))) {
                    if (currentPhone != null) {
                        sqlSession.update("selling.customer.phone.block", currentPhone);
                    }
                    phone.setPhoneId(IDGenerator.generate("PNM"));
                    phone.setCustomer(customer);
                    sqlSession.insert("selling.customer.phone.insert", phone);
                }
                CustomerAddress address = customer.getAddress();
                CustomerAddress currentAddress = sqlSession.selectOne("selling.customer.address.query", condition);
                if (address != null || (currentAddress != null && !address.getAddress().equals(currentAddress.getAddress()))) {
                    if (currentAddress != null) {
                        sqlSession.update("selling.customer.address.block", currentAddress);
                    }
                    address.setAddressId(IDGenerator.generate("ADR"));
                    address.setCustomer(customer);
                    sqlSession.insert("selling.customer.address.insert", address);
                }
                //查询当前持久化的客户信息
                condition.remove("blockFlag");
                customer = sqlSession.selectOne("selling.customer.query", condition);
                result.setData(customer);
                sqlSession.commit();
            } catch (Exception e) {
                sqlSession.rollback();
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    @Transactional
    @Override
    public ResultData deleteCustomer(Customer customer) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("selling.customer.delete", customer);
            result.setData(customer);
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
            List<Customer> list = sqlSession.selectList(
                    "selling.customer.query", condition);
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

    @Override
    public ResultData queryCustomerByPage(Map<String, Object> condition,
                                          DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<Customer> page = new DataTablePage<Customer>();
        page.setsEcho(param.getsEcho());
        ResultData total = queryCustomer(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            logger.error(result.getDescription());
        }
        page.setiTotalRecords(((List<Customer>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<Customer>) total.getData()).size());
        List<Customer> current = queryCustomerByPage(condition,
                param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    private List<Customer> queryCustomerByPage(Map<String, Object> condition,
                                               int start, int length) {
        List<Customer> result = new ArrayList<Customer>();
        try {
            result = sqlSession.selectList("selling.customer.query", condition,
                    new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData queryCustomerPhone(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CustomerPhone> list = sqlSession.selectList(
                    "selling.customer.phoneQuery", condition);
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

    @Override
    public ResultData queryCustomerAddress(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            condition = handle(condition);
            List<CustomerAddress> list = sqlSession.selectList(
                    "selling.customer.address.query", condition);
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
