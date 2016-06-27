package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.StatisticDao;
import selling.sunshine.model.sum.Sum4Order;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.List;

/**
 * Created by sunshine on 6/24/16.
 */
@Repository
public class StatisticDaoImpl extends BaseDao implements StatisticDao {
    private Logger logger = LoggerFactory.getLogger(StatisticDaoImpl.class);

    @Override
    public ResultData queryOrderSum() {
        ResultData result = new ResultData();
        try {
            List<Sum4Order> list = sqlSession.selectList("selling.statistic.sum4order");
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
