package selling.sunshine.service;

import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;
import selling.sunshine.model.AgentVitality;

import java.util.Map;

public interface AgentVitalityService {
	
	ResultData createAgentVitality(AgentVitality agentVitality);
	
	ResultData fetchAgentVitality(Map<String, Object> condition);

	/*查询代理商活跃度配置*/
	ResultData fetchAgentVitalityByPage(Map<String, Object> condition, DataTableParam param);
	
	ResultData updateAgentVitality(AgentVitality agentVitality);

}
