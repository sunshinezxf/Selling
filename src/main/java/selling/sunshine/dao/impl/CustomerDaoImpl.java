package selling.sunshine.dao.impl;

import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.customer.CustomerAddress;
import common.sunshine.model.selling.customer.CustomerPhone;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import selling.sunshine.dao.CustomerDao;
import selling.sunshine.utils.TencentMapAPI;
import selling.sunshine.vo.customer.CustomerVo;

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
                Map<String, String> map = TencentMapAPI.getDetailInfoByAddress(address.getAddress());
                if (map.containsKey("province")) {
                    address.setProvince(map.get("province"));
                }
                if (map.containsKey("city")) {
                    address.setCity(map.get("city"));
                }
                if (map.containsKey("district")) {
                    address.setDistrict(map.get("district"));
                }
                sqlSession.insert("selling.customer.address.insert", address);
                //从数据库中获取刚插入的记录
                Map<String, Object> condition = new HashMap<>();
                condition.put("customerId", customer.getCustomerId());
                customer = sqlSession.selectOne("selling.customer.query", condition);
                result.setData(customer);
            } catch (Exception e) {
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
                sqlSession.update("selling.customer.update", customer);//更新customer表本身
                Map<String, Object> condition = new HashMap<>();
                condition.put("customerId", customer.getCustomerId());
                condition.put("blockFlag", false);
                //如果新的电话号码与数据库中最新使用的号码不同,则弃用原来的号码并新增一条记录
                CustomerPhone phone = customer.getPhone();
                CustomerPhone currentPhone = sqlSession.selectOne("selling.customer.phone.query", condition);
                if (phone != null) {
                    if (currentPhone == null || (currentPhone != null && !currentPhone.getPhone().equals(phone.getPhone()))) {
                        phone.setPhoneId(IDGenerator.generate("PNM"));
                        phone.setCustomer(customer);
                        sqlSession.insert("selling.customer.phone.insert", phone);
                        if (currentPhone != null) {
                            sqlSession.update("selling.customer.phone.block", currentPhone);
                        }
                    }
                }
                CustomerAddress address = customer.getAddress();
                CustomerAddress currentAddress = sqlSession.selectOne("selling.customer.address.query", condition);
                if (address != null) {
                    if (currentAddress == null || (currentAddress != null && !address.getAddress().equals(currentAddress.getAddress()))) {
                        address.setAddressId(IDGenerator.generate("ADR"));
                        address.setCustomer(customer);
                        Map<String, String> map = TencentMapAPI.getDetailInfoByAddress(address.getAddress());
                        if (map.containsKey("province")) {
                            address.setProvince(map.get("province"));
                        }
                        if (map.containsKey("city")) {
                            address.setCity(map.get("city"));
                        }
                        if (map.containsKey("district")) {
                            address.setDistrict(map.get("district"));
                        }
                        sqlSession.insert("selling.customer.address.insert", address);
                        if (currentAddress != null) {
                            sqlSession.update("selling.customer.address.block", currentAddress);
                        }
                    }
                }
                //查询当前持久化的客户信息
                condition.remove("blockFlag");
                customer = sqlSession.selectOne("selling.customer.query", condition);
                result.setData(new CustomerVo(customer));
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    /**
     * 删除顾客信息(标记删除)
     *
     * @param customer
     * @return
     */
    @Transactional
    @Override
    public ResultData deleteCustomer(Customer customer) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.customer.delete", customer);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    /**
     * 查询符合查询条件的顾客列表
     *
     * @param condition
     * @return
     */
    @Override
    public ResultData queryCustomer(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CustomerVo> list = sqlSession.selectList("selling.customer.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    /**
     * Data table分页查询顾客信息列表
     *
     * @param condition
     * @param param
     * @return
     */
    @Override
    public ResultData queryCustomerByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<CustomerVo> page = new DataTablePage<>();
        page.setsEcho(param.getsEcho());
        condition = handle(condition);
        if (!StringUtils.isEmpty(param.getsSearch())) {
            condition.put("search", "%" + param.getsSearch() + "%");
        }
        ResultData total = queryCustomer(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List<CustomerVo>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<CustomerVo>) total.getData()).size());
        List<CustomerVo> current = queryCustomerByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    /**
     * 查询某一页的顾客列表
     *
     * @param condition
     * @param start
     * @param length
     * @return
     */
    private List<CustomerVo> queryCustomerByPage(Map<String, Object> condition, int start, int length) {
        List<CustomerVo> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.customer.query", condition, new RowBounds(start, length));
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
            List<CustomerPhone> list = sqlSession.selectList("selling.customer.phone.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
            }
            result.setData(list);
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
            List<CustomerAddress> list = sqlSession.selectList("selling.customer.address.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData updateCustomerAddress(CustomerAddress customerAddress) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.customer.address.update", customerAddress);
                result.setData(customerAddress);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }


}
