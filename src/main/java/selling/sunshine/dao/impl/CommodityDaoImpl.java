package selling.sunshine.dao.impl;

import org.apache.ibatis.session.RowBounds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.CommodityDao;
import selling.sunshine.model.Goods;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
@Repository
public class CommodityDaoImpl extends BaseDao implements CommodityDao {
    private Logger logger = LoggerFactory.getLogger(CommodityDaoImpl.class);

    private Object lock = new Object();

    @Transactional
    public ResultData insertCommodity(Goods goods) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                goods.setGoodsId(IDGenerator.generate("COM"));
                sqlSession.insert("selling.goods.insert", goods);
                result.setData(goods);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result = insertCommodity(goods);
            } finally {
                return result;
            }
        }
    }

    @Override
    public ResultData queryCommodity(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Goods> list = sqlSession.selectList("selling.goods.query", condition);
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
    public ResultData queryCommodityByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<Goods> page = new DataTablePage<Goods>();
        page.setsEcho(param.getsEcho());
        ResultData total = queryCommodity(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List<Goods>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<Goods>) total.getData()).size());
        List<Goods> current = queryCommodityByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    private List<Goods> queryCommodityByPage(Map<String, Object> condition, int start, int length) {
        List<Goods> result = new ArrayList<Goods>();
        try {
            result = sqlSession.selectList("selling.goods.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }
}
