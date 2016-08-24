package promotion.sunshine.dao;

import java.util.Map;

import common.sunshine.utils.ResultData;

/**
 * Created by sunshine on 8/23/16.
 */
public interface EventDao {
	ResultData queryGiftEvent(Map<String, Object> condition);
	
}
