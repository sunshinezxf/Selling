package selling.sunshine.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import common.sunshine.dao.BaseDao;
import selling.sunshine.dao.CashBackDao;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.vo.cashback.CashBack4Agent;
import selling.sunshine.vo.cashback.CashBack4AgentPerMonth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 8/10/16.
 */
@Repository
public class CashBackDaoImpl extends BaseDao implements CashBackDao {
    private Logger logger = LoggerFactory.getLogger(CashBackDaoImpl.class);

    @Override
    public ResultData queryMonthly(Map<String, Object> condition) {
        ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<CashBack4AgentPerMonth> list = sqlSession.selectList("selling.cashback.summary.query4Month", condition);
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
    public ResultData queryMonthlyByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<CashBack4AgentPerMonth> page = new DataTablePage<>();
        page.setsEcho(param.getsEcho());
        ResultData total = queryMonthly(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List) total.getData()).size());
        page.setiTotalDisplayRecords(((List) total.getData()).size());
        List<CashBack4AgentPerMonth> current = queryMonthlyByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<CashBack4Agent> list = sqlSession.selectList("selling.cashback.summary.query", condition);
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
    public ResultData queryByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<CashBack4Agent> page = new DataTablePage<>();
        page.setsEcho(param.getsEcho());
        ResultData total = query(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List) total.getData()).size());
        page.setiTotalDisplayRecords(((List) total.getData()).size());
        List<CashBack4Agent> current = queryByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        result.setData(current);
        result.setData(page);
        return result;
    }

    private List<CashBack4AgentPerMonth> queryMonthlyByPage(Map<String, Object> condition, int start, int length) {
        List<CashBack4AgentPerMonth> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.cashback.summary.query4Month", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

    private List<CashBack4Agent> queryByPage(Map<String, Object> condtion, int start, int length) {
        List<CashBack4Agent> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.cashback.summary.query", condtion, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }
}
