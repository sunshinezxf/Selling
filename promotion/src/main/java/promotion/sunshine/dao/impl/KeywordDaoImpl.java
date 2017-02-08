package promotion.sunshine.dao.impl;

import common.sunshine.dao.BaseDao;
import common.sunshine.mybatis.DataSourceContextHolder;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import promotion.sunshine.dao.KeywordDao;
import promotion.sunshine.model.Keyword;

import java.util.List;
import java.util.Map;

/**
 * Created by wxd on 2017/2/7.
 */
public class KeywordDaoImpl extends BaseDao implements KeywordDao {

    private Logger logger = LoggerFactory.getLogger(KeywordDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertKeyword(Keyword keyword) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                DataSourceContextHolder.setDbType("event");
                keyword.setWordId(IDGenerator.generate("KEY"));
                sqlSession.insert("promotion.keyword.insert", keyword);
                result.setData(keyword);
                DataSourceContextHolder.clearDbType();
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
    public ResultData queryKeyword(Map<String, Object> condition) {
        ResultData result = new ResultData();
        condition = handle(condition);
        try {
            DataSourceContextHolder.setDbType("event");
            List<Keyword> list = sqlSession.selectList("promotion.keyword.query", condition);
            result.setData(list);
            DataSourceContextHolder.clearDbType();
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }
}
