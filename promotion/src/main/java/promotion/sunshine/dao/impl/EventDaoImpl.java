package promotion.sunshine.dao.impl;

import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.event.GiftEvent;
import common.sunshine.model.selling.event.QuestionAnswer;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import promotion.sunshine.dao.EventDao;

/**
 * Created by sunshine on 8/23/16.
 */
@Repository
public class EventDaoImpl extends BaseDao implements EventDao {
    private Logger logger = LoggerFactory.getLogger(EventDaoImpl.class);
    
    private Object lock = new Object();

	@Override
	public ResultData queryGiftEvent(Map<String, Object> condition) {
		ResultData result = new ResultData();
		condition = handle(condition);
		try {
			List<GiftEvent> list = sqlSession.selectList("promotion.event.query", condition);
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		} finally {
			return result;
		}
	}
	
	@Override
	public ResultData queryQuestionOption(Map<String, Object> condition) {
		ResultData result = new ResultData();
		condition = handle(condition);
		try {
			List<GiftEvent> list = sqlSession.selectList("promotion.event.question.option.query", condition);
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		} finally {
			return result;
		}
	}

	@Override
	public ResultData insertEventApplication(EventApplication eventApplication) {
		ResultData result = new ResultData();
        synchronized (lock) {
            try {
                eventApplication.setApplicationId(IDGenerator.generate("EVA"));
                sqlSession.insert("promotion.event.application.insert", eventApplication);
                result.setData(eventApplication);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }	
    }

	@Override
	public ResultData queryEventApplication(Map<String, Object> condition) {
		ResultData result = new ResultData();
		condition = handle(condition);
		try {
			List<GiftEvent> list = sqlSession.selectList("promotion.event.application.query", condition);
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		} finally {
			return result;
		}
	}

	@Override
	public ResultData insertQuestionAnswer(QuestionAnswer questionAnswer) {
		ResultData result = new ResultData();
        synchronized (lock) {
            try {
                questionAnswer.setAnswerId(IDGenerator.generate("QAS"));
                sqlSession.insert("promotion.event.question.answer.insert", questionAnswer);
                result.setData(questionAnswer);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }	
		
	}

	
}
