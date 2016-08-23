package selling.sunshine.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.sunshine.dao.BaseDao;
import selling.sunshine.dao.OrderPoolDao;
import selling.sunshine.model.OrderPool;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

public class OrderPoolDaoImpl extends BaseDao implements OrderPoolDao {
	private Logger logger = LoggerFactory.getLogger(OrderItemDaoImpl.class);

	private Object lock = new Object();
	
	@Override
	public ResultData queryOrderPool(Map<String, Object> condition) {
		ResultData result = new ResultData();
		synchronized(lock){
			try{
				List<OrderPool> list = sqlSession.selectList("selling.order.pool.query", condition);
				result.setData(list);
			}catch (Exception e) {
	            logger.error(e.getMessage());
	            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	            result.setDescription(e.getMessage());
	        } finally {
	            return result;
	        }
		}
	}

}
