package selling.sunshine.service;

import java.util.Map;

import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.event.GiftEvent;
import common.sunshine.model.selling.event.PromotionEvent;
import common.sunshine.model.selling.event.support.PromotionConfig;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

public interface EventService {
	
	ResultData fetchEvent(Map<String, Object> condition);
	
	ResultData fetchEvent(Map<String, Object> condition, DataTableParam param);
	
	ResultData createGiftEvent(GiftEvent event);
	
	ResultData createPromotionEvent(PromotionEvent event);
	
	ResultData fetchPromotionEvent(Map<String, Object> condition);
	
	ResultData fetchGiftEvent(Map<String, Object> condition);
	
	//ResultData updateGiftEvent(GiftEvent event);
	
	//ResultData fetchGiftEventByPage(Map<String, Object> condition, DataTableParam param);
	
	ResultData fetchEventApplication(Map<String, Object> condition);
	
	ResultData fetchEventApplicationByPage(Map<String, Object> condition, DataTableParam param);
	
	ResultData updateEventApplication(EventApplication eventApplication);
	
	ResultData createEventOrder(EventOrder eventOrder);
	
	ResultData updateEventOrder(EventOrder eventOrder);
	
	ResultData fetchEventOrder(Map<String, Object> condition);
	
	ResultData fetchEventOrderByPage(Map<String, Object> condition, DataTableParam param);

	ResultData createPromotionConfig(PromotionConfig promotionConfig);
	
	ResultData fetchPromotionConfig(Map<String, Object> condition);
	
	ResultData fetchPromotionConfigByPage(Map<String, Object> condition, DataTableParam param);


}
