package selling.sunshine.dao;

import java.util.Map;

import selling.sunshine.model.BackOperationLog;
import selling.sunshine.utils.ResultData;

public interface BackOperationLogDao {
	ResultData insertBackOperationLog(BackOperationLog backOperationLog);
	
	ResultData queryBackOperationLog(Map<String, Object> condition);
}
