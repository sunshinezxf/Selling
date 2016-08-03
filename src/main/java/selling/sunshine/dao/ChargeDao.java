package selling.sunshine.dao;

import selling.sunshine.model.Charge;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 8/3/16.
 */
public interface ChargeDao {
    ResultData insertCharge(Charge charge);

    ResultData queryCharge(Map<String, Object> condition);
}
