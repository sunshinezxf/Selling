package selling.sunshine.service;

import common.sunshine.model.selling.agent.Agent;
import selling.sunshine.model.BankCard;
import common.sunshine.model.selling.agent.Credit;
import selling.sunshine.model.WithdrawRecord;
import selling.sunshine.model.gift.GiftApply;
import selling.sunshine.model.gift.GiftConfig;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

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

    /*用代理商的余额来付钱 */
    ResultData consume(Agent agent, double money);

    /*解绑代理商（跟微信号）*/
    ResultData unbindAgent(String openId);

    /*重置代理商登陆密码*/
    ResultData resetPassword(Agent agent);

    /*修改代理商登陆密码*/
    ResultData modifyPassword(Agent agent, String password);

    /*重置代理商群规模*/
    ResultData modifyScale(Agent agent);

    /*查询代理商身份证照片*/
    ResultData fetchCredit(Map<String, Object> condition);

    ResultData createCredit(Credit credit);

    ResultData updateCredit(Credit credit);

    /*申请提现*/
    ResultData applyWithdraw(WithdrawRecord record);

    /*查询代理商赠送的配置*/
    ResultData fetchAgentGift(Map<String, Object> condition);

    ResultData updateAgentGift(GiftConfig giftConfig);

    ResultData updateAgentGift(List<GiftConfig> giftConfigs);

    ResultData createAgentGift(GiftConfig giftConfig);

    /*添加代理商赠送申请*/
    ResultData createGiftApply(GiftApply apply);

    ResultData fetchGiftApply(Map<String, Object> condition);

    ResultData updateGiftApply(GiftApply apply);

    ResultData fetchGiftApply(Map<String, Object> condition, DataTableParam param);

    /*拒绝赠送申请*/
    ResultData declineGiftApply(GiftApply apply);

    ResultData queryWithdraw(Map<String, Object> condition);

    ResultData fetchBankCard(Map<String, Object> condition);

    ResultData modifyBankCard(BankCard bankCard);
}
