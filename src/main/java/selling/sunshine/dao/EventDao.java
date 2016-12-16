package selling.sunshine.dao;

import java.util.Map;

import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.event.GiftEvent;
import common.sunshine.model.selling.event.PromotionEvent;
import common.sunshine.model.selling.event.support.PromotionConfig;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

public interface EventDao {
	
	ResultData queryEvent(Map<String, Object> condition);
	
	ResultData queryEventByPage(Map<String, Object> condition, DataTableParam param);
	
	ResultData queryGiftEvent(Map<String, Object> condition);
	
	ResultData queryPromotionEvent(Map<String, Object> condition);
	
	ResultData insertGiftEvent(GiftEvent event);
	
	ResultData insertPromotionEvent(PromotionEvent event);
	
	//ResultData updateGiftEvent(GiftEvent event);
	
	//ResultData queryGiftEventByPage(Map<String, Object> condition, DataTableParam param);
	
	ResultData queryEventApplication(Map<String, Object> condition);
	
	ResultData queryEventApplication(Map<String, Object> condition, DataTableParam param);
	
	ResultData updateEventApplication(EventApplication eventApplication);
	
	ResultData insertEventOrder(EventOrder eventOrder);
	
	ResultData updateEventOrder(EventOrder eventOrder);
	
	ResultData queryEventOrder(Map<String, Object> condition);
	
	ResultData queryEventOrderByPage(Map<String, Object> condition, DataTableParam param);
	
	ResultData insertPromotionConfig(PromotionConfig promotionConfig);
	
	ResultData queryPromotionConfig(Map<String, Object> condition);
	
	ResultData queryPromotionConfigByPage(Map<String, Object> condition, DataTableParam param);

}
