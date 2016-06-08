package selling.sunshine.service;

import java.util.Map;

import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

public interface WithdrawService {
	
	ResultData fetchWithdrawRecord(Map<String, Object> condition, DataTableParam param);

}
