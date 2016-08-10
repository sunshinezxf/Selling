package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.ChargeDao;
import selling.sunshine.model.Charge;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 8/3/16.
 */
@Repository
public class ChargeDaoImpl extends BaseDao implements ChargeDao {
    private Logger logger = LoggerFactory.getLogger(ChargeDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertCharge(Charge charge) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.insert("selling.charge.insert", charge);
                result.setData(charge);
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
    public ResultData queryCharge(Map<String, Object> condition) {
        ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<Charge> list = sqlSession.selectList("selling.charge.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
