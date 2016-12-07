package selling.sunshine.dao;

import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 6/24/16.
 */
public interface StatisticDao {
    ResultData queryOrderSum();

    ResultData orderStatistics(Map<String, Object> condition);

    ResultData orderStatisticsByPage(DataTableParam param);

    ResultData agentGoodsMonthByPage(DataTableParam param);

    ResultData agentGoodsMonth(Map<String, Object> condition);

    ResultData agentGoodsByPage(DataTableParam param);

    ResultData agentGoods(Map<String, Object> condition);

    ResultData orderMonth();

    ResultData orderLastYear(Map<String, Object> condition);

    ResultData topThreeAgent();

    ResultData purchaseRecord(Map<String, Object> condition);

    ResultData queryVolume(Map<String, Object> condition);
    
    ResultData queryAgentGoods(Map<String, Object> condition);
    
    ResultData agentRanking(Map<String, Object> condition);
    
    ResultData purchaseRecordEveryday();
    
    ResultData purchaseRecordEveryMonth();

}
