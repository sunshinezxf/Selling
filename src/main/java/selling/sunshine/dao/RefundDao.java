package selling.sunshine.dao;

import selling.sunshine.model.RefundConfig;
import selling.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/17/16.
 */
public interface RefundDao {
    ResultData insertRefundConfig(RefundConfig config);

    ResultData queryRefundConfig(Map<String, Object> condition);
    
    ResultData queryRefundRecord(Map<String, Object> condition);
    
    ResultData queryRefundRecordByPage(Map<String, Object> condition,DataTableParam param);
    
    ResultData refundRecord();
    
    ResultData refund();
    
    ResultData statistic(Map<String, Object> condition);
    
    ResultData calculateRefund(String agentId);
    
    ResultData calculateQuantity(String agentId);
    
    ResultData calculateQuantityAll(String agentId);
}
