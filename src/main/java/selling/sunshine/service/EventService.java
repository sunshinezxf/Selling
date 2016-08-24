package selling.sunshine.service;

import java.util.Map;

import common.sunshine.model.selling.event.Event;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

public interface EventService {
	
	ResultData createEvent(Event event);
	
	ResultData fetchEvent(Map<String, Object> condition);
	
	ResultData updateEvent(Event event);
	
	ResultData fetchEventByPage(Map<String, Object> condition, DataTableParam param);

}
