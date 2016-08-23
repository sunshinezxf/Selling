package selling.sunshine.dao;

import selling.sunshine.model.WithdrawRecord;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

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
