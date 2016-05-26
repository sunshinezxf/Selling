package selling.sunshine.service;

import selling.sunshine.model.RefundConfig;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/17/16.
 */
public interface RefundService {
    ResultData createRefundConfig(RefundConfig config);

    ResultData fetchRefundConfig(Map<String, Object> condition);
    
    public ResultData refundRecord();
}
