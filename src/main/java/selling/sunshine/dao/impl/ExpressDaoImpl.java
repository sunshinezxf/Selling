package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.ExpressDao;
import selling.sunshine.model.express.Express;
import selling.sunshine.model.express.Express4Agent;
import selling.sunshine.model.express.Express4Customer;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

@Repository
public class ExpressDaoImpl extends BaseDao implements ExpressDao {
    private Logger logger = LoggerFactory.getLogger(ExpressDaoImpl.class);

    private Object lock = new Object();

    /**
     * 添加快递单记录
     *
     * @param express
     * @return
     */
    @Transactional
    @Override
    public ResultData insertExpress4Agent(Express4Agent express) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                express.setExpressId(IDGenerator.generate("EPA"));
                sqlSession.insert("selling.express.insertExpress4Agent",express);
                result.setData(express);
            } catch (Exception e) {
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
    public ResultData insertExpress4Customer(Express4Customer express) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                express.setExpressId(IDGenerator.generate("EPC"));
                sqlSession.insert("selling.express.insertExpress4Customer",express);
                result.setData(express);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 根据条件查询快递列表
     *
     * @param condition
     * @return
     */
    @Override
    public ResultData queryExpress(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Express> list = sqlSession.selectList("selling.express.query", condition);
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
    public ResultData queryExpress4Agent(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Express> list = sqlSession.selectList("selling.express.query4Agent", condition);
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
    public ResultData queryExpress4Customer(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Express4Agent> list = sqlSession.selectList("selling.express.query4Customer", condition);
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
