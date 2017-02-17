package promotion.sunshine.dao;

import java.util.Map;

import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.event.QuestionAnswer;
import common.sunshine.utils.ResultData;

/**
 * Created by sunshine on 8/23/16.
 */
public interface EventDao {
	
	//查询赠送活动
	ResultData queryGiftEvent(Map<String, Object> condition);
	
	//查询赠送活动多选题选项
	ResultData queryQuestionOption(Map<String, Object> condition);
	
	//插入赠送活动
	ResultData insertEventApplication(EventApplication eventApplication);
	
	//查询活动申请
	ResultData queryEventApplication(Map<String, Object> condition);
	
	//插入赠送活动多选题回答
	ResultData insertQuestionAnswer(QuestionAnswer questionAnswer);
}
