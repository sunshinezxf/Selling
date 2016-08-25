package promotion.sunshine.service;

import java.util.Map;

import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.utils.ResultData;

public interface EventService {
	ResultData fetchGiftEvent(Map<String, Object> condition);
	
	ResultData insertEventApplication(EventApplication eventApplication);
}
