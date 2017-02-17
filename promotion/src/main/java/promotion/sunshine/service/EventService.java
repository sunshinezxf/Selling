package promotion.sunshine.service;

import java.util.Map;

import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.event.QuestionAnswer;
import common.sunshine.utils.ResultData;

public interface EventService {
	
	//获取赠送活动
	ResultData fetchGiftEvent(Map<String, Object> condition);
	
	//获取多选题选项
	ResultData fetchQuestionOption(Map<String, Object> condition);
	
	//插入活动申请
	ResultData insertEventApplication(EventApplication eventApplication);
	
	//获取活动申请
	ResultData fetchEventApplication(Map<String, Object> condition);
	
	//插入问题答案
	ResultData insertQuestionAnswer(QuestionAnswer questionAnswer);
}
