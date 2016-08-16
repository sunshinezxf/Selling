package selling.sunshine.service;

import selling.sunshine.model.Agent;
import selling.sunshine.model.BankCard;
import selling.sunshine.model.Credit;
import selling.sunshine.model.WithdrawRecord;
import selling.sunshine.model.gift.GiftApply;
import selling.sunshine.model.gift.GiftConfig;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
public interface AgentService {
    ResultData login(Agent agent);

    ResultData fetchAgent(Map<String, Object> condition);

    ResultData createAgent(Agent agent);

    ResultData fetchAgent(Map<String, Object> condition, DataTableParam param);

    ResultData updateAgent(Agent agent);

    ResultData consume(Agent agent, double money);

    ResultData unbindAgent(String openId);

    ResultData resetPassword(Agent agent);

    ResultData modifyPassword(Agent agent, String password);

    ResultData modifyScale(Agent agent);

    ResultData fetchCredit(Map<String, Object> condition);

    ResultData fetchBankCard(Map<String, Object> condition);

    ResultData modifyBankCard(BankCard bankCard);

    ResultData createCredit(Credit credit);

    ResultData updateCredit(Credit credit);

    ResultData applyWithdraw(WithdrawRecord record);

    ResultData queryWithdraw(Map<String, Object> condition);

    ResultData fetchAgentGift(Map<String, Object> condition);

    ResultData updateAgentGift(GiftConfig giftConfig);

    ResultData updateAgentGift(List<GiftConfig> giftConfigs);

    ResultData createAgentGift(GiftConfig giftConfig);

    ResultData createGiftApply(GiftApply apply);

    ResultData fetchGiftApply(Map<String, Object> condition);

    ResultData updateGiftApply(GiftApply apply);

    ResultData fetchGiftApply(Map<String, Object> condition, DataTableParam param);

    ResultData declineGiftApply(GiftApply apply);
}
