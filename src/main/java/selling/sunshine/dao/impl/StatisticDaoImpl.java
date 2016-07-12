package selling.sunshine.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.StatisticDao;
import selling.sunshine.model.sum.OrderStatistics;
import selling.sunshine.model.sum.Sum4Order;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.ArrayList;
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

	@Override
	public ResultData orderStatistics() {
		ResultData result = new ResultData();
		try {
		   List<OrderStatistics> list=sqlSession.selectList("selling.statistic.sumOrderMonth");
		   result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
	}
	
	private List<OrderStatistics> orderStatisticsByPage(int start, int length){
		List<OrderStatistics> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.statistic.sumOrderMonth", null, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
	}

	@Override
	public ResultData orderStatisticsByPage(DataTableParam param) {
		ResultData result = new ResultData();
		DataTablePage<OrderStatistics> page = new DataTablePage<>();
		ResultData total = orderStatistics();
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List) total.getData()).size());
        page.setiTotalDisplayRecords(((List) total.getData()).size());
        List<OrderStatistics> current=orderStatisticsByPage( param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
	}
}
