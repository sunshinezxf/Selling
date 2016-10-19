package selling.sunshine.service;

import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 6/24/16.
 */
public interface StatisticService {
    ResultData query4OrderSum();

    ResultData orderStatisticsByPage(DataTableParam param);

    ResultData agentGoodsMonthByPage(DataTableParam param);

    ResultData agentGoodsByPage(DataTableParam param);

    ResultData orderMonth();

    ResultData orderByYear();

    ResultData topThreeAgent();

    ResultData purchaseRecord(Map<String, Object> condition);

    ResultData fetchVolume(Map<String, Object> condition);
    
    ResultData queryAgentGoods(Map<String, Object> condition);
    
    ResultData agentRanking(Map<String, Object> condition);
    
    ResultData purchaseRecordEveryday();
    
    ResultData purchaseRecordEveryMonth();

}
