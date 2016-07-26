package selling.sunshine.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mysql.fabric.xmlrpc.base.Array;

import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.StatisticDao;
import selling.sunshine.model.sum.AgentGoods;
import selling.sunshine.model.sum.OrderMonth;
import selling.sunshine.model.sum.OrderSeries;
import selling.sunshine.model.sum.OrderStatistics;
import selling.sunshine.model.sum.Sum4Order;
import selling.sunshine.model.sum.TopThreeAgent;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
	
	private List<AgentGoods> agentGoodsMonthByPage(int start, int length){
		List<AgentGoods> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.statistic.sumAgentGoodsMonth", null, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
	}
	
	private List<AgentGoods> agentGoodsByPage(int start, int length){
		List<AgentGoods> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.statistic.sumAgentGoods", null, new RowBounds(start, length));
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

	@Override
	public ResultData agentGoodsMonthByPage(DataTableParam param) {
		ResultData result = new ResultData();
		DataTablePage<AgentGoods> page = new DataTablePage<>();
		ResultData total = agentGoodsMonth();
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List) total.getData()).size());
        page.setiTotalDisplayRecords(((List) total.getData()).size());
        List<AgentGoods> current=agentGoodsMonthByPage( param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
	}

	@Override
	public ResultData agentGoodsMonth() {
		ResultData result = new ResultData();
		try {
		   List<AgentGoods> list=sqlSession.selectList("selling.statistic.sumAgentGoodsMonth");
		   result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
	}
	
	@Override
	public ResultData agentGoods() {
		ResultData result = new ResultData();
		try {
		   List<AgentGoods> list=sqlSession.selectList("selling.statistic.sumAgentGoods");
		   result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
	}

	@Override
	public ResultData agentGoodsByPage(DataTableParam param) {
		ResultData result = new ResultData();
		DataTablePage<AgentGoods> page = new DataTablePage<>();
		ResultData total = agentGoods();
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List) total.getData()).size());
        page.setiTotalDisplayRecords(((List) total.getData()).size());
        List<AgentGoods> current=agentGoodsByPage( param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
	}

	@Override
	public ResultData orderMonth() {
		ResultData result = new ResultData();
		try {
			List<OrderMonth> list=sqlSession.selectList("selling.statistic.orderMonth");
			result.setData(list);
		} catch (Exception e) {
			 logger.error(e.getMessage());
	         result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	         result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData orderByYear() {
		ResultData result = new ResultData();
		try {
			 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		     String date = dateFormat.format(timestamp);
		     int start=2016;
		     int end=Integer.parseInt(date);
		     Map<String, Object> condition=new HashMap<>();
		     List<OrderSeries> list=new ArrayList<>();
		     while ((end-start)>=0) {
				String year=String.valueOf(end);
				condition.put("year", year);
				List<Map<String, Object>> query = sqlSession.selectList("selling.statistic.orderByYear", condition);
				if (query.size()!=0) {
					int[] data={0,0,0,0,0,0,0,0,0,0,0,0};
					for(int i=0;i<query.size();i++){
						data[Integer.parseInt(query.get(i).get("date").toString().split("-")[1])-1]=Integer.parseInt(query.get(i).get("amount").toString());
					}
					OrderSeries orderSeries=new OrderSeries(year, data);
					list.add(orderSeries);
				}
				end--;				
			 }			
			result.setData(list);
		} catch (Exception e) {
			 logger.error(e.getMessage());
	         result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	         result.setDescription(e.getMessage());
		}
		return result;
	}

	@Override
	public ResultData topThreeAgent() {
		ResultData result = new ResultData();
		try {
			List<Object> list=new ArrayList<>();
			List<TopThreeAgent> monthList=new ArrayList();
			List<TopThreeAgent> allList=new ArrayList();
			List<TopThreeAgent> list1=sqlSession.selectList("selling.statistic.topThreeAgentMonth");
			List<TopThreeAgent> list2=sqlSession.selectList("selling.statistic.topThreeAgent");
			if (list1.size()>3) {
				monthList.add(list1.get(0));
				monthList.add(list1.get(1));
				monthList.add(list1.get(2));
				for (int i = 3; i < list1.size(); i++) {
					if(list1.get(i).getQuantity()==list1.get(2).getQuantity()){
						monthList.add(list1.get(i));
					}else{
						break;
					}
				}
			}
			if (list2.size()>3) {
				allList.add(list2.get(0));
				allList.add(list2.get(1));
				allList.add(list2.get(2));
				for (int i = 3; i < list2.size(); i++) {
					if(list2.get(i).getQuantity()==list2.get(2).getQuantity()){
						allList.add(list2.get(i));
					}else{
						break;
					}
				}
			}
			list.add(monthList);
			list.add(allList);
			result.setData(list);
		} catch (Exception e) {
			 logger.error(e.getMessage());
	         result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	         result.setDescription(e.getMessage());
		}
		return result;
	}


}
