package selling.sunshine.dao.impl;

import common.sunshine.utils.IDGenerator;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import common.sunshine.dao.BaseDao;
import selling.sunshine.dao.WithdrawDao;
import selling.sunshine.model.WithdrawRecord;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 6/6/16.
 */
@Repository
public class WithdrawDaoImpl extends BaseDao implements WithdrawDao {
    private Logger logger = LoggerFactory.getLogger(WithdrawDaoImpl.class);

    private Object lock = new Object();

    @Transactional
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

    @Transactional
    @Override
    public ResultData updateWithdraw(WithdrawRecord record) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.agent.withdraw.update", record);
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
    public ResultData queryWithdrawByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<WithdrawRecord> page = new DataTablePage<>(param);
        condition = handle(condition);
        ResultData total = queryWithdraw(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List<WithdrawRecord>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<WithdrawRecord>) total.getData()).size());
        List<WithdrawRecord> current = queryWithdrawByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    private List<WithdrawRecord> queryWithdrawByPage(Map<String, Object> condition, int start, int length) {
        List<WithdrawRecord> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.agent.withdraw.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData statistic(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Map<String, Object>> list = sqlSession.selectList("selling.agent.withdraw.statistic", condition);
            result.setData(list);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData money(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            Double money = sqlSession.selectOne("selling.agent.withdraw.money", condition);
            result.setData(money);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }
}
