package selling.sunshine.service;

import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 8/3/16.
 */
public interface ChargeService {
    ResultData fectchCharge(Map<String, Object> condition);
}
