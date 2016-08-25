package promotion.sunshine.dao;

import java.util.Map;

import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.utils.ResultData;

/**
 * Created by sunshine on 8/23/16.
 */
public interface EventDao {
	ResultData queryGiftEvent(Map<String, Object> condition);
	
	ResultData insertEventApplication(EventApplication eventApplication);
	
}
