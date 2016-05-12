package selling.sunshine.dao;

import selling.sunshine.model.Agent;
import selling.sunshine.model.Order;
import selling.sunshine.model.OrderItem;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
public interface AgentDao {
    ResultData insertAgent(Agent agent);

    ResultData queryAgent(Map<String, Object> condition);

    ResultData queryAgentByPage(Map<String, Object> condition, DataTableParam param);

    ResultData updateAgent(Agent agent);
    
    ResultData updateAgent(Agent agent);
    
}
