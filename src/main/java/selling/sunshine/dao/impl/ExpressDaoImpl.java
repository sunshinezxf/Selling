package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.ExpressDao;
import selling.sunshine.model.Express;
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
    public ResultData insertExpress(Express express) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                express.setExpressId(IDGenerator.generate("EXP"));
                sqlSession.insert("selling.express.insert", express);
                result.setData(express);
                sqlSession.commit();
            } catch (Exception e) {
                sqlSession.rollback();
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                logger.error(e.getMessage());
                result = insertExpress(express);
            } finally {
                return result;
            }
        }
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
}
