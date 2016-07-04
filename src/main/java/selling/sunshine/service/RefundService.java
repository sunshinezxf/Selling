package selling.sunshine.service;

import selling.sunshine.model.RefundConfig;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/17/16.
 */
public interface RefundService {
    ResultData createRefundConfig(RefundConfig config);

    ResultData fetchRefundConfig(Map<String, Object> condition);
    
    ResultData refundRecord();
    
    ResultData fetchRefundRecord(Map<String, Object> condition);
    
    ResultData fetchRefundRecordByPage(Map<String, Object> condition,DataTableParam param);
    
    ResultData refund();
    
    ResultData statistic(Map<String, Object> condition);
    
    ResultData calculateRefund(String agentId);
}
