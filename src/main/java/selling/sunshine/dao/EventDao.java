package selling.sunshine.dao;

import java.util.Map;

import common.sunshine.model.selling.event.Event;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

public interface EventDao {
	
	ResultData insertEvent(Event event);
	
	ResultData queryEvent(Map<String, Object> condition);
	
	ResultData updateEvent(Event event);
	
	ResultData queryEventByPage(Map<String, Object> condition, DataTableParam param);

}
