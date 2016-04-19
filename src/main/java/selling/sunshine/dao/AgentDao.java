package selling.sunshine.dao;

import selling.sunshine.model.Agent;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 4/8/16.
 */
public interface AgentDao {
    ResultData insertAgent(Agent agent);
}
