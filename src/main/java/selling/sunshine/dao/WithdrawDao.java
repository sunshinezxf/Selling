package selling.sunshine.dao;

import selling.sunshine.model.WithdrawRecord;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 6/6/16.
 */
public interface WithdrawDao {
    ResultData insertWithdraw(WithdrawRecord record);
}
