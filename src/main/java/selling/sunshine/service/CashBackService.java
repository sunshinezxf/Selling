package selling.sunshine.service;

import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 8/12/16.
 */
public interface CashBackService {
    ResultData fetchCashBackPerMonth(Map<String, Object> condition, DataTableParam param);
}
