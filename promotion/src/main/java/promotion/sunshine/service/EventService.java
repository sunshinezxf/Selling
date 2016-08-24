package promotion.sunshine.service;

import java.util.Map;

import common.sunshine.utils.ResultData;

public interface EventService {
	ResultData fetchGiftEvent(Map<String, Object> condition);
	
	ResultData insertEventApplication();
}
