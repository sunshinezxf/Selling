package selling.sunshine.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.sunshine.dao.BaseDao;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.AgentVitalityDao;
import selling.sunshine.model.AgentVitality;
import selling.sunshine.model.RefundConfig;

public class AgentVitalityDaoImpl extends BaseDao implements AgentVitalityDao {

	private Logger logger = LoggerFactory.getLogger(AgentVitalityDaoImpl.class);

	private Object lock = new Object();

	@Override
	public ResultData insertAgentVitality(AgentVitality agentVitality) {
		ResultData result = new ResultData();        
        synchronized (lock) {
            try {
                Map<String, Object> condition = new HashMap<>();
                condition.put("agentVitalityId", agentVitality.getAgentVitalityId());
                RefundConfig target = sqlSession.selectOne("selling.agent.vitality.query", condition);
                if (target != null) {
                    target.setBlockFlag(true);
                    sqlSession.update("selling.refund.config.block", target);
                }
                agentVitality.setAgentVitalityId(IDGenerator.generate("VIT"));
                sqlSession.insert("selling.agent.vitality.insert", agentVitality);
                result.setData(agentVitality);
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
	public ResultData queryAgentVitality(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<AgentVitality> list = sqlSession.selectList("selling.agent.vitality.query", condition);
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
	public ResultData queryAgentVitalityByPage(Map<String, Object> condition, DataTableParam param) {
		 ResultData result = new ResultData();
	        DataTablePage<AgentVitality> page = new DataTablePage<>(param);
	        condition = handle(condition);
	        ResultData total = queryAgentVitality(condition);
	        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
	            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	            result.setDescription(total.getDescription());
	            return result;
	        }
	        page.setiTotalRecords(((List<AgentVitality>) total.getData()).size());
	        page.setiTotalDisplayRecords(((List<AgentVitality>) total.getData()).size());
	        List<AgentVitality> current = queryAgentVitalityByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
	        if (current.isEmpty()) {
	            result.setResponseCode(ResponseCode.RESPONSE_NULL);
	        }
	        page.setData(current);
	        result.setData(page);
	        return result;
	}
	
    private List<AgentVitality> queryAgentVitalityByPage(Map<String, Object> condition, int start, int length) {
        List<AgentVitality> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.agent.vitality.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

	@Override
	public ResultData updateAgentVitality(AgentVitality agentVitality) {
		// TODO Auto-generated method stub
		return null;
	}

}
