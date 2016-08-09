package selling.sunshine.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.GiftApplyDao;
import selling.sunshine.model.Agent;
import selling.sunshine.model.gift.GiftApply;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 8/9/16.
 */
@Repository
public class GiftApplyDaoImpl extends BaseDao implements GiftApplyDao {
    private Logger logger = LoggerFactory.getLogger(GiftApplyDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertGiftApply(GiftApply apply) {
        ResultData result = new ResultData();
        apply.setApplyId(IDGenerator.generate("GFA"));
        synchronized (lock) {
            try {
                sqlSession.insert("selling.gift.apply.insert", apply);
                result.setData(apply);
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
    public ResultData queryGiftApply(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            condition = handle(condition);
            List<Agent> list = sqlSession.selectList("selling.gift.apply.query", condition);
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
    public ResultData queryGiftApplyByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<GiftApply> page = new DataTablePage<>(param);
        condition = handle(condition);
        ResultData total = queryGiftApply(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List<GiftApply>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<GiftApply>) total.getData()).size());
        List<GiftApply> current = queryGiftApplyByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    private List<GiftApply> queryGiftApplyByPage(Map<String, Object> condition, int start, int length) {
        List<GiftApply> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.agent.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

}
