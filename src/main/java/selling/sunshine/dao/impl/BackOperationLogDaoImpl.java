package selling.sunshine.dao.impl;

import common.sunshine.utils.IDGenerator;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selling.sunshine.dao.BackOperationLogDao;
import common.sunshine.dao.BaseDao;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.pagination.MobilePage;
import selling.sunshine.pagination.MobilePageParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BackOperationLogDaoImpl extends BaseDao implements BackOperationLogDao {
    private Logger logger = LoggerFactory.getLogger(BackOperationLogDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertBackOperationLog(BackOperationLog backOperationLog) {
        ResultData result = new ResultData();
        backOperationLog.setLogId(IDGenerator.generate("BOL"));
        synchronized (lock) {
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

    @Override
    public ResultData queryBackOperationLog(Map<String, Object> condition, MobilePageParam param) {
        ResultData result = new ResultData();
        MobilePage<BackOperationLog> page = new MobilePage<>();
        condition = handle(condition);
        ResultData total = queryBackOperationLog(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setTotal(((List<BackOperationLog>) total.getData()).size());
        List<BackOperationLog> current = queryBackOperationLog(condition, param.getStart(), param.getLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;

    }

    private List<BackOperationLog> queryBackOperationLog(Map<String, Object> condition, int start, int length) {
        List<BackOperationLog> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.backoperationlog.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
