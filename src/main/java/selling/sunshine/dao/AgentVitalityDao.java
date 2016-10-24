package selling.sunshine.dao;

import java.util.Map;

import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;
import selling.sunshine.model.AgentVitality;

public interface AgentVitalityDao {
	
	ResultData insertAgentVitality(AgentVitality agentVitality);
	
	ResultData queryAgentVitality(Map<String, Object> condition);
	
	ResultData queryAgentVitalityByPage(Map<String, Object> condition, DataTableParam param);
	
	ResultData updateAgentVitality(AgentVitality agentVitality);
	
	

}
