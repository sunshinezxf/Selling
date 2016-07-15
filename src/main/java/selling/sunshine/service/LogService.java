package selling.sunshine.service;

import java.util.Map;

import selling.sunshine.model.BackOperationLog;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.utils.ResultData;

public interface LogService {
	ResultData fetchBackOperationLog(Map<String, Object> condition);

	ResultData fetchBackOperationLog(Map<String, Object> condition, MobilePageParam param);

	ResultData createbackOperationLog(BackOperationLog backOperationLog);
}
