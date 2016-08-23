package selling.sunshine.service;

import selling.sunshine.model.WithdrawRecord;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

public interface WithdrawService {
    ResultData createWithdrawRecord(WithdrawRecord record);

    ResultData fetchWithdrawRecord(Map<String, Object> condition, DataTableParam param);

    ResultData fetchWithdrawRecord(Map<String, Object> condition);

    ResultData updateWithdrawRecord(WithdrawRecord record);

    ResultData statistic(Map<String, Object> condition);

    ResultData fetchSumMoney(Map<String, Object> condition);
    
    ResultData produceApply(List<WithdrawRecord> list);
    
    ResultData produceSummary(List<WithdrawRecord> list);
}
