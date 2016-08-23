package selling.sunshine.service;

import selling.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;
import selling.sunshine.vo.cashback.CashBack4AgentPerMonth;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 8/12/16.
 */
public interface CashBackService {
    ResultData fetchCashBackPerMonth(Map<String, Object> condition);

    ResultData fetchCashBackPerMonth(Map<String, Object> condition, DataTableParam param);

    ResultData fetchCashBack(Map<String, Object> condition, DataTableParam param);

    ResultData produce(List<CashBack4AgentPerMonth> list);
}
