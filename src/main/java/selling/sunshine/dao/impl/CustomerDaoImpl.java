package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.CustomerDao;
import selling.sunshine.model.Agent;
import selling.sunshine.model.Customer;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/5/16.
 */
@Repository
public class CustomerDaoImpl extends BaseDao implements CustomerDao {

	private Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);

	@Override
	public ResultData insertCustomer(Customer customer, Agent agent) {
		ResultData result = new ResultData();

		return result;
	}

	@Override
	public ResultData updateCustomer(Customer customer, Agent agent) {
		ResultData result = new ResultData();
		try {
			sqlSession.update("selling.customer", customer);
			sqlSession.commit();
			sqlSession.close();
			result.setData(customer);
			result.setResponseCode(ResponseCode.RESPONSE_OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
		}finally{
			return result;
		}	
		
	}
}
