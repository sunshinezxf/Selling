package selling.sunshine.dao;

import selling.sunshine.model.BackOperationLog;
import common.sunshine.pagination.MobilePageParam;
import common.sunshine.utils.ResultData;

import java.util.Map;

public interface BackOperationLogDao {
    ResultData insertBackOperationLog(BackOperationLog backOperationLog);

    ResultData queryBackOperationLog(Map<String, Object> condition);

    ResultData queryBackOperationLog(Map<String, Object> condition, MobilePageParam param);
}
