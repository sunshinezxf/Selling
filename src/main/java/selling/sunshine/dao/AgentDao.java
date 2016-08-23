package selling.sunshine.dao;

import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.agent.Credit;
import selling.sunshine.model.gift.GiftConfig;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

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

    ResultData updateAgentScale(Agent agent);

    ResultData unbindAgent(String openId);

    ResultData queryCredit(Map<String, Object> condition);

    ResultData insertCredit(Credit credit);

    ResultData updateCredit(Credit credit);

    ResultData insertAgentGift(GiftConfig config);

    ResultData queryAgentGift(Map<String, Object> condition);

    ResultData updateAgentGift(GiftConfig config);

    ResultData updateAgentGift(List<GiftConfig> list);
}
