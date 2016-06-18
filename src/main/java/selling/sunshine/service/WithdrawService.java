package selling.sunshine.service;

import java.util.Map;

import selling.sunshine.model.WithdrawRecord;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

public interface WithdrawService {
	ResultData createWithdrawRecord(WithdrawRecord record);

	ResultData fetchWithdrawRecord(Map<String, Object> condition, DataTableParam param);
	
	ResultData fetchWithdrawRecord(Map<String, Object> condition);

	ResultData updateWithdrawRecord(Map<String, Object> condition);
}
