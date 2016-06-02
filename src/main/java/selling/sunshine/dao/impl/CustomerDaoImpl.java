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
import selling.sunshine.model.Goods;
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
				phoneNumber.setCustomer(customer);
				address.setCustomer(customer);
				phoneNumber.setPhoneId(IDGenerator.generate("PNM"));
				address.setAddressId(IDGenerator.generate("ADD"));
				sqlSession.insert("selling.customer.phone.insert", phoneNumber);
				sqlSession.insert("selling.customer.address.insert", address);
				address.setCustomer(null);
				phoneNumber.setCustomer(null);
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
			customer.getPhone().setPhoneId(IDGenerator.generate("PNW"));
	        customer.getAddress().setAddressId(IDGenerator.generate("ADD"));

			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("customerId", customer.getCustomerId());
			ResultData resultData = queryCustomer(condition);

			CustomerPhone phoneNumber = customer.getPhone();
			CustomerAddress address = customer.getAddress();
			
			phoneNumber.setCustomer(customer);
			address.setCustomer(customer);

			sqlSession.insert("selling.customer.phone.insert", phoneNumber);
			sqlSession.insert("selling.customer.address.insert", address);
			Customer customer2 = ((List<Customer>) resultData.getData()).get(0);

			sqlSession.update("selling.customer.phone.block",
					customer2.getPhone());
			sqlSession.update("selling.customer.address.block",
					customer2.getAddress());

			customer= ((List<Customer>)queryCustomer(condition).getData()).get(0);
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
	public ResultData updateCustomerAddress(Customer customer) {
		ResultData result = new ResultData();
		try{
			customer.getAddress().setAddressId(IDGenerator.generate("ADD"));
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("customerId", customer.getCustomerId());
			ResultData resultData = queryCustomer(condition);

			CustomerAddress address = customer.getAddress();
			
			address.setCustomer(customer);

			sqlSession.insert("selling.customer.address.insert", address);
			Customer customer2 = ((List<Customer>) resultData.getData()).get(0);

			sqlSession.update("selling.customer.address.block",
					customer2.getAddress());

			customer= ((List<Customer>)queryCustomer(condition).getData()).get(0);
			result.setData(customer);
			result.setResponseCode(ResponseCode.RESPONSE_OK);

		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
		} finally {
			return result;
		}
	}
	
	@Transactional
	@Override
	public ResultData deleteCustomer(Customer customer) {
		ResultData result = new ResultData();
		try{
			sqlSession.update("selling.customer.delete",customer);
			result.setData(customer);
		} catch (Exception e){
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
