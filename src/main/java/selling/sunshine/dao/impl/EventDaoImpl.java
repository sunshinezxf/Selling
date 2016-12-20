package selling.sunshine.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.event.*;
import common.sunshine.model.selling.event.support.PromotionConfig;
import common.sunshine.model.selling.order.EventOrder;
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
import selling.sunshine.dao.EventDao;

import java.util.ArrayList;
import java.util.HashMap;
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
                sqlSession.insert("selling.event.insertGiftEvent", event);
                List<EventQuestion> questions = event.getQuestions();
                event.setQuestions(null);
                for (EventQuestion question : questions) {
                    question.setEvent(event);
                    question.setQuestionId(IDGenerator.generate("EQN"));
                    sqlSession.insert("selling.event.question.insert", question);
                    List<QuestionOption> options = question.getOptions();
                    question.setOptions(null);
                    for (QuestionOption option : options) {
                        EventQuestion temp = new EventQuestion();
                        temp.setQuestionId(question.getQuestionId());
                        option.setQuestion(question);
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
    public ResultData insertPromotionEvent(PromotionEvent event) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                event.setEventId(IDGenerator.generate("PRE"));
                sqlSession.insert("selling.event.insertPromotionEvent", event);
                List<PromotionConfig> configs = event.getConfig();
                event.setConfig(null);
                for (PromotionConfig promotionConfig : configs) {
                    promotionConfig.setConfigId(IDGenerator.generate("PRC"));
                    promotionConfig.setEvent(event);
                    sqlSession.insert("selling.event.promotion.config.insert", promotionConfig);
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
            List<GiftEvent> list = sqlSession.selectList("selling.event.queryGiftEvent", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

//	@Override
//	public ResultData updateGiftEvent(GiftEvent event) {
//		ResultData result = new ResultData();
//		try {
//			sqlSession.update("selling.event.update", event);
//			result.setData(event);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//			result.setDescription(e.getMessage());
//		} finally {
//			return result;
//		}
//	}

    @Override
    public ResultData queryEvent(Map<String, Object> condition) {
        ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<Event> list = sqlSession.selectList("selling.event.query", condition);
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
    public ResultData queryEventByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<Event> page = new DataTablePage<>(param);
        condition = handle(condition);
        ResultData total = queryEvent(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List<Event>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<Event>) total.getData()).size());
        List<Event> current = queryEventByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    private List<Event> queryEventByPage(Map<String, Object> condition, int start, int length) {
        List<Event> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.event.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

//	@Override
//	public ResultData queryGiftEventByPage(Map<String, Object> condition, DataTableParam param) {
//		ResultData result = new ResultData();
//		DataTablePage<GiftEvent> page = new DataTablePage<>(param);
//		condition = handle(condition);
//		ResultData total = queryGiftEvent(condition);
//		if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
//			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//			result.setDescription(total.getDescription());
//			return result;
//		}
//		page.setiTotalRecords(((List<GiftEvent>) total.getData()).size());
//		page.setiTotalDisplayRecords(((List<GiftEvent>) total.getData()).size());
//		List<GiftEvent> current = queryGiftEventByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
//		if (current.isEmpty()) {
//			result.setResponseCode(ResponseCode.RESPONSE_NULL);
//		}
//		page.setData(current);
//		result.setData(page);
//		return result;
//	}
//
//	private List<GiftEvent> queryGiftEventByPage(Map<String, Object> condition, int start, int length) {
//		List<GiftEvent> result = new ArrayList<>();
//		try {
//			result = sqlSession.selectList("selling.event.queryGiftEvent", condition, new RowBounds(start, length));
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		} finally {
//			return result;
//		}
//	}

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
        List<EventApplication> current = queryEventApplication(condition, param.getiDisplayStart(),
                param.getiDisplayLength());
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

    @Override
    public ResultData queryPromotionEvent(Map<String, Object> condition) {
        ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<PromotionEvent> list = sqlSession.selectList("selling.event.queryPromotionEvent", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
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
    public ResultData insertPromotionConfig(PromotionConfig promotionConfig) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                Map<String, Object> condition = new HashMap<>();
                condition.put("configId", promotionConfig.getConfigId());
                PromotionConfig target = sqlSession.selectOne("selling.event.promotion.config.query", condition);
                if (target != null) {
                    target.setBlockFlag(true);
                    sqlSession.update("selling.event.promotion.config.block", target);
                }
                promotionConfig.setConfigId(IDGenerator.generate("PRC"));
                sqlSession.insert("selling.event.promotion.config.insert", promotionConfig);
                result.setData(promotionConfig);
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
    public ResultData queryPromotionConfig(Map<String, Object> condition) {
        ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<PromotionConfig> list = sqlSession.selectList("selling.event.promotion.config.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryPromotionConfigByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<PromotionConfig> page = new DataTablePage<>();
        page.setsEcho(param.getsEcho());
        logger.debug(JSONObject.toJSONString(condition));

        ResultData total = queryPromotionConfig(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List<PromotionConfig>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<PromotionConfig>) total.getData()).size());
        List<PromotionConfig> current = queryPromotionConfigByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    private List<PromotionConfig> queryPromotionConfigByPage(Map<String, Object> condition, int start, int length) {
        List<PromotionConfig> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.event.promotion.config.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

}
