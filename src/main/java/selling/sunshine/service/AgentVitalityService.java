package selling.sunshine.service;

import java.util.Map;

import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;
import selling.sunshine.model.AgentVitality;

public interface AgentVitalityService {
	
	ResultData createAgentVitality(AgentVitality agentVitality);
	
	ResultData fetchAgentVitality(Map<String, Object> condition);
	
	ResultData fetchAgentVitalityByPage(Map<String, Object> condition, DataTableParam param);
	
	ResultData updateAgentVitality(AgentVitality agentVitality);

}
