package selling.sunshine.dao;

import java.util.Map;

import common.sunshine.model.selling.agent.AgentKPI;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

public interface AgentKPIDao {
	
	ResultData insertAgentKPI(AgentKPI agentKPI);
	
	ResultData updateAgentKPI(AgentKPI agentKPI);
	
	ResultData queryAgentKPI(Map<String, Object> condition);
	
	ResultData queryAgentKPIByPage(Map<String, Object> condition, DataTableParam param);
	

}
