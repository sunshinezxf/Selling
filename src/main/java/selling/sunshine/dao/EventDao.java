package selling.sunshine.dao;

import java.util.Map;

import common.sunshine.model.selling.event.GiftEvent;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

public interface EventDao {
	
	ResultData insertGiftEvent(GiftEvent event);
	
	ResultData queryGiftEvent(Map<String, Object> condition);
	
	ResultData updateGiftEvent(GiftEvent event);
	
	ResultData queryGiftEventByPage(Map<String, Object> condition, DataTableParam param);
	
	ResultData queryEventApplication(Map<String, Object> condition);
	
	ResultData queryEventApplication(Map<String, Object> condition, DataTableParam param);

}
