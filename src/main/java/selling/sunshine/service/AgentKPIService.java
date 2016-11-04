package selling.sunshine.service;

import java.util.Map;

import common.sunshine.model.selling.agent.AgentKPI;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

public interface AgentKPIService {
	
    ResultData createAgentKPI(AgentKPI agentKPI);
	
	ResultData updateAgentKPI(AgentKPI agentKPI);
	
	ResultData fetchAgentKPI(Map<String, Object> condition);
	
	ResultData fetchAgentKPIByPage(Map<String, Object> condition, DataTableParam param);

}
