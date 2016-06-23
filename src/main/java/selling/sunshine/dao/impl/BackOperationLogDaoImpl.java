package selling.sunshine.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import selling.sunshine.dao.BackOperationLogDao;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.model.BankCard;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

public class BackOperationLogDaoImpl extends BaseDao implements BackOperationLogDao {
	private Logger logger = LoggerFactory.getLogger(BackOperationLogDaoImpl.class);

	private Object lock = new Object();
	
	@Override
	public ResultData insertBackOperationLog(BackOperationLog backOperationLog) {
		ResultData result = new ResultData();
		backOperationLog.setLogId(IDGenerator.generate("BOL"));
		synchronized(lock){
			try {
                sqlSession.insert("selling.backoprationlog.insert", backOperationLog);
                result.setData(backOperationLog);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
		}
	}

	@Override
	public ResultData queryBackOperationLog(Map<String, Object> condition) {
		ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<BackOperationLog> list = sqlSession.selectList("selling.backoprationlog.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
	}

}
