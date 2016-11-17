package selling.sunshine.dao;

import common.sunshine.model.selling.agent.AgentKPI;
import common.sunshine.model.selling.customer.CustomerAddress;
import common.sunshine.utils.ResultData;

public interface ScheduleDao {
	
	ResultData updateCustomerAddress(CustomerAddress customerAddress);
	
	ResultData insertAgentKPI(AgentKPI agentKPI);

}
