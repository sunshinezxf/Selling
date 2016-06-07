package selling.sunshine.service;

import selling.sunshine.model.Agent;
import selling.sunshine.model.Credit;
import selling.sunshine.model.WithdrawRecord;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
public interface AgentService {
    ResultData login(Agent agent);

    ResultData fetchAgent(Map<String, Object> condition);

    ResultData createAgent(Agent agent);

    ResultData fetchAgent(Map<String, Object> condition, DataTableParam param);

    ResultData placeOrder(Map<String, Object> conditon);

    ResultData updateAgent(Agent agent);

    ResultData consume(Agent agent, double money);

    ResultData unbindAgent(String openId);

    ResultData resetPassword(Agent agent);

    ResultData modifyPassword(Agent agent, String password);

    ResultData fetchCredit(Map<String, Object> condition);

    ResultData createCredit(Credit credit);

    ResultData applyWithdraw(WithdrawRecord record);
}
