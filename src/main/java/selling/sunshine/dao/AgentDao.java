package selling.sunshine.dao;

import selling.sunshine.model.Agent;
import selling.sunshine.model.Credit;
import selling.sunshine.model.gift.GiftConfig;
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

    ResultData updateAgentCoffer(Agent agent);

    ResultData unbindAgent(String openId);

    ResultData queryCredit(Map<String, Object> condition);

    ResultData insertCredit(Credit credit);

    ResultData insertAgentGift(GiftConfig config);

    ResultData queryAgentGift(Map<String, Object> condition);

    ResultData updateAgentGift(GiftConfig config);

    ResultData updateAgentGift(List<GiftConfig> list);
}
