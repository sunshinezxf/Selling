package selling.sunshine.service;

import selling.sunshine.model.Agent;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
public interface AgentService {
    ResultData fetchAgent(Map<String, Object> condition);

    ResultData createAgent(Agent agent);

    ResultData fetchAgent(Map<String, Object> condition, DataTableParam param);
    
    ResultData placeOrder(Map<String, Object> conditon);
    
    ResultData updateAgent(Agent agent);
}
