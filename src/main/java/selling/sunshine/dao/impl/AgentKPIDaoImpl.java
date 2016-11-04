package selling.sunshine.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.agent.AgentKPI;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.AgentKPIDao;


public class AgentKPIDaoImpl extends BaseDao implements AgentKPIDao {

	private Logger logger = LoggerFactory.getLogger(AgentKPIDaoImpl.class);

	private Object lock = new Object();

	@Override
	public ResultData insertAgentKPI(AgentKPI agentKPI) {
		ResultData result = new ResultData();
		synchronized (lock) {
			try {
				agentKPI.setKpiId(IDGenerator.generate("KPI"));
				sqlSession.insert("selling.agent.kpi.insert", agentKPI);
				result.setData(agentKPI);
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
	public ResultData updateAgentKPI(AgentKPI agentKPI) {
		ResultData result = new ResultData();
		synchronized (lock) {
			try {
				sqlSession.update("selling.agent.kpi.update", agentKPI);
				result.setData(agentKPI);
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
	public ResultData queryAgentKPI(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<AgentKPI> list = sqlSession.selectList("selling.agent.kpi.query", condition);
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
	public ResultData queryAgentKPIByPage(Map<String, Object> condition, DataTableParam param) {
		 ResultData result = new ResultData();
	        DataTablePage<AgentKPI> page = new DataTablePage<>(param);
	        condition = handle(condition);
	        ResultData total = queryAgentKPI(condition);
	        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
	            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	            result.setDescription(total.getDescription());
	            return result;
	        }
	        page.setiTotalRecords(((List<AgentKPI>) total.getData()).size());
	        page.setiTotalDisplayRecords(((List<AgentKPI>) total.getData()).size());
	        List<AgentKPI> current = queryAgentKPIByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
	        if (current.isEmpty()) {
	            result.setResponseCode(ResponseCode.RESPONSE_NULL);
	        }
	        page.setData(current);
	        result.setData(page);
	        return result;
	}
	
    private List<AgentKPI> queryAgentKPIByPage(Map<String, Object> condition, int start, int length) {
        List<AgentKPI> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.agent.kpi.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

}
