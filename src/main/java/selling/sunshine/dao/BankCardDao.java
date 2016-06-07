package selling.sunshine.dao;

import selling.sunshine.model.BankCard;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 6/7/16.
 */
public interface BankCardDao {
    ResultData createBankCard(BankCard card);

    ResultData queryBankCard(Map<String, Object> condition);
}
