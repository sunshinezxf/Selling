package selling.sunshine.dao.impl;

import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.event.EventQuestion;
import common.sunshine.model.selling.event.GiftEvent;
import common.sunshine.model.selling.event.QuestionOption;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import selling.sunshine.dao.EventDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class EventDaoImpl extends BaseDao implements EventDao {
    private Logger logger = LoggerFactory.getLogger(EventDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertGiftEvent(GiftEvent event) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
            	event.setEventId(IDGenerator.generate("GEV"));
                sqlSession.insert("selling.event.insert", event);
                List<EventQuestion> questions = event.getQuestions();
                for (EventQuestion question : questions) {
                	
                	question.setQuestionId(IDGenerator.generate("EQN"));
                	sqlSession.insert("selling.event.question.insert",question);
                    List<QuestionOption> options = question.getOptions();
                    for (QuestionOption option : options) {
                        EventQuestion temp = new EventQuestion();
                        temp.setQuestionId(question.getQuestionId());
                        option.setQuestion(temp);
                        option.setOptionId(IDGenerator.generate("OPT"));
                        sqlSession.insert("selling.event.question.option.insert", option);
                    }
                  
                }
                
                result.setData(event);
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
    public ResultData queryGiftEvent(Map<String, Object> condition) {
        ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<GiftEvent> list = sqlSession.selectList("selling.event.query", condition);
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
    public ResultData updateGiftEvent(GiftEvent event) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("selling.event.update", event);
            result.setData(event);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData queryGiftEventByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<GiftEvent> page = new DataTablePage<>(param);
        condition = handle(condition);
        ResultData total = queryGiftEvent(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List<GiftEvent>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<GiftEvent>) total.getData()).size());
        List<GiftEvent> current = queryGiftEventByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    private List<GiftEvent> queryGiftEventByPage(Map<String, Object> condition, int start, int length) {
        List<GiftEvent> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.event.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData queryEventApplication(Map<String, Object> condition) {
        ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<EventApplication> list = sqlSession.selectList("selling.event.application.query", condition);
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
    public ResultData queryEventApplication(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<EventApplication> page = new DataTablePage<>(param);
        condition = handle(condition);
        if (!StringUtils.isEmpty(param.getsSearch())) {
            String searchParam = param.getsSearch().replace("/", "-");
            condition.put("search", "%" + searchParam + "%");
        }
        if (!StringUtils.isEmpty(param.getParams())) {
            JSONObject json = JSON.parseObject(param.getParams());
            if (json.containsKey("status")) {
                switch (json.getString("status")) {
                    case "HANDLING":
                    	condition.put("status", 0);
                        break;
                    case "AGREE":
                    	condition.put("status", 2);
                        break;
                    case "REJECT":
                    	condition.put("status", 1);
                        break;
                }
            }
        }
        ResultData total = queryEventApplication(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List<EventApplication>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<EventApplication>) total.getData()).size());
        List<EventApplication> current = queryEventApplication(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    private List<EventApplication> queryEventApplication(Map<String, Object> condition, int start, int length) {
        List<EventApplication> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.event.application.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

	@Override
	public ResultData updateEventApplication(EventApplication eventApplication) {
		ResultData result = new ResultData();
        try {
            sqlSession.update("selling.event.application.update", eventApplication);
            result.setData(eventApplication);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
	}

	@Override
	public ResultData insertEventOrder(EventOrder eventOrder) {
		ResultData result = new ResultData();
        synchronized (lock) {
            try {
            	eventOrder.setOrderId(IDGenerator.generate("EOI"));
                sqlSession.insert("selling.event.order.insert", eventOrder);    
                result.setData(eventOrder);
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
	public ResultData updateEventOrder(EventOrder eventOrder) {
		 ResultData result = new ResultData();
	        try {
	            sqlSession.update("selling.event.order.update", eventOrder);
	            result.setData(eventOrder);
	        } catch (Exception e) {
	            logger.error(e.getMessage());
	            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	            result.setDescription(e.getMessage());
	        } finally {
	            return result;
	        }
	}

	@Override
	public ResultData queryEventOrder(Map<String, Object> condition) {
		 ResultData result = new ResultData();
	        condition = handle(condition);
	        try {
	            List<EventOrder> list = sqlSession.selectList("selling.event.order.query", condition);
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
	public ResultData queryEventOrderByPage(Map<String, Object> condition, DataTableParam param) {
		 ResultData result = new ResultData();
	        DataTablePage<EventOrder> page = new DataTablePage<>(param);
	        condition = handle(condition);
	        if (!StringUtils.isEmpty(param.getsSearch())) {
	            String searchParam = param.getsSearch().replace("/", "-");
	            condition.put("search", "%" + searchParam + "%");
	        }
	        if (!StringUtils.isEmpty(param.getParams())) {
	            JSONObject json = JSON.parseObject(param.getParams());
	            if (json.containsKey("status")) {
	            	List<Integer> statusList = new ArrayList<>();
	                switch (json.getString("status")) {
	                    case "PAYED":
	                		statusList.add(1);
	                		condition.put("status", statusList);
	                        break;
	                    case "SENT":
	                    	statusList = new ArrayList<>();
	                		statusList.add(2);
	                		condition.put("status", statusList);
	                        break;
	                    case "RECEIVED":
	                    	statusList = new ArrayList<>();
	                		statusList.add(3);
	                		condition.put("status", statusList);
	                        break;
	                }
	            }
	        }
	        ResultData total = queryEventOrder(condition);
	        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
	            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	            result.setDescription(total.getDescription());
	            return result;
	        }
	        page.setiTotalRecords(((List<EventOrder>) total.getData()).size());
	        page.setiTotalDisplayRecords(((List<EventOrder>) total.getData()).size());
	        List<EventOrder> current = queryEventOrder(condition, param.getiDisplayStart(), param.getiDisplayLength());
	        if (current.isEmpty()) {
	            result.setResponseCode(ResponseCode.RESPONSE_NULL);
	        }
	        page.setData(current);
	        result.setData(page);
	        return result;
	}
	
	private List<EventOrder> queryEventOrder(Map<String, Object> condition, int start, int length) {
		List<EventOrder> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.event.order.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
	}

}
