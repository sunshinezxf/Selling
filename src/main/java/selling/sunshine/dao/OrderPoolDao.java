package selling.sunshine.dao;

import common.sunshine.utils.ResultData;

import java.util.Map;

public interface OrderPoolDao {
	ResultData queryOrderPool(Map<String, Object> condition);
	
	ResultData checkOrderPool(Map<String, Object> condition);

}
