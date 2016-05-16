package selling.sunshine.dao;

import selling.sunshine.model.ShipConfig;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/13/16.
 */
public interface ShipmentDao {
    ResultData insertShipmentConfig(ShipConfig config);

    ResultData insertShipmentConfig(List<ShipConfig> list);

    ResultData queryShipmentConfig(Map<String, Object> condition);
}
