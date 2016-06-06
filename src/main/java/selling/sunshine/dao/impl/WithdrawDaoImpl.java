package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.WithdrawDao;
import selling.sunshine.model.WithdrawRecord;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 6/6/16.
 */
@Repository
public class WithdrawDaoImpl extends BaseDao implements WithdrawDao {
    private Logger logger = LoggerFactory.getLogger(WithdrawDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertWithdraw(WithdrawRecord record) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                record.setWithdrawId(IDGenerator.generate("WID"));
                sqlSession.insert("selling.agent.withdraw.insert", record);
                result.setData(record);
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
    public ResultData queryWithdraw(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            condition = handle(condition);
            List<WithdrawRecord> list = sqlSession.selectList("selling.agent.withdraw.query", condition);
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
