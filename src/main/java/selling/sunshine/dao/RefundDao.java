package selling.sunshine.dao;

import selling.sunshine.model.RefundConfig;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/17/16.
 */
public interface RefundDao {
    ResultData insertRefundConfig(RefundConfig config);

    ResultData queryRefundConfig(Map<String, Object> condition);
}
