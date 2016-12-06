package selling.sunshine.dao.impl;

import common.sunshine.utils.IDGenerator;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import common.sunshine.dao.BaseDao;
import selling.sunshine.dao.GiftApplyDao;
import common.sunshine.model.selling.agent.Agent;
import selling.sunshine.model.gift.GiftApply;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

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
    public ResultData updateGiftApply(GiftApply apply) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.gift.apply.update", apply);
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
    public ResultData queryGiftApplyByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<GiftApply> page = new DataTablePage<>(param);
        condition = handle(condition);
        if (!StringUtils.isEmpty(param.getsSearch())) {  
        	condition.put("search", "%"+param.getsSearch()+"%");
 		}
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

    @Override
    public ResultData blockGiftApply(GiftApply apply) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.gift.apply.block", apply);
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

    private List<GiftApply> queryGiftApplyByPage(Map<String, Object> condition, int start, int length) {
        List<GiftApply> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.gift.apply.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

}
