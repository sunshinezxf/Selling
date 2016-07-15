package selling.sunshine.dao;

import selling.sunshine.model.BackOperationLog;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

public interface BackOperationLogDao {
    ResultData insertBackOperationLog(BackOperationLog backOperationLog);

    ResultData queryBackOperationLog(Map<String, Object> condition);

    ResultData queryBackOperationLog(Map<String, Object> condition, MobilePageParam param);
}
