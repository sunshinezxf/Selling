package selling.sunshine.service;

import selling.sunshine.model.Charge;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 8/3/16.
 */
public interface ChargeService {
    ResultData fectchCharge(Map<String, Object> condition);

    ResultData reimburse(Charge charge);
}
