package selling.sunshine.dao;

import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 8/10/16.
 */
public interface CashBackDao {
    ResultData queryMonthly(Map<String, Object> condition);

    ResultData queryMonthlyByPage(Map<String, Object> condition, DataTableParam param);

    ResultData query(Map<String, Object> condition);

    ResultData queryByPage(Map<String, Object> condition, DataTableParam param);

	ResultData queryALL(Map<String, Object> condition);
}
