package selling.sunshine.dao;

import java.util.Map;

import selling.sunshine.utils.ResultData;

public interface OrderPoolDao {
	ResultData queryOrderPool(Map<String, Object> condition);
}
