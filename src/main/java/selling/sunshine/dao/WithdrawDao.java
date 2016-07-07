package selling.sunshine.dao;

import selling.sunshine.model.WithdrawRecord;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 6/6/16.
 */
public interface WithdrawDao {
    ResultData insertWithdraw(WithdrawRecord record);

    ResultData queryWithdraw(Map<String, Object> condition);

    ResultData updateWithdraw(WithdrawRecord record);

    ResultData queryWithdrawByPage(Map<String, Object> condition, DataTableParam param);
    
    ResultData statistic(Map<String, Object> condition);

    ResultData money(Map<String, Object> condition);
}
