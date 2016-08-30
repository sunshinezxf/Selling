package selling.sunshine.controller;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.event.Event;
import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.event.EventQuestion;
import common.sunshine.model.selling.event.GiftEvent;
import common.sunshine.model.selling.event.QuestionOption;
import common.sunshine.model.selling.event.support.ApplicationStatus;
import common.sunshine.model.selling.event.support.ChoiceType;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.form.EventQuestionForm;
import selling.sunshine.form.GiftEventForm;
import selling.sunshine.service.EventService;

/**
 * Created by sunshine on 8/23/16.
 */
@RestController
@RequestMapping("/event")
public class EventController {
	private Logger logger = LoggerFactory.getLogger(EventController.class);

	@Autowired
	private EventService eventService;

	@RequestMapping(method = RequestMethod.GET, value = "/create")
	public ModelAndView create() {
		ModelAndView view = new ModelAndView();
		view.setViewName("backend/event/create");
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/create")
	public ResultData create(@RequestBody GiftEventForm form, HttpSession session) {
		ResultData resultData = new ResultData();
		Map<String, Object> condition=new HashMap<>();
		condition.put("blockFlag", false);
		ResultData queryResult=eventService.fetchGiftEvent(condition);
		if (queryResult.getResponseCode()==ResponseCode.RESPONSE_OK) {
			resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
			return resultData;
		}
		GiftEvent giftEvent = new GiftEvent();
		giftEvent.setTitle(form.getGiftEventTitle());
		giftEvent.setNickname(form.getGiftEventNickname());
		giftEvent.setStart(Timestamp.valueOf(form.getStartTime() + ":00"));
		giftEvent.setEnd(Timestamp.valueOf(form.getEndTime() + ":00"));
		List<EventQuestion> eventQuestions = new ArrayList<>();
		for (int i = 0; i < form.getQuestionList().length; i++) {
			EventQuestionForm eventQuestionForm = form.getQuestionList()[i];
			EventQuestion eventQuestion = new EventQuestion();
			eventQuestion.setContent(eventQuestionForm.getContent());
			eventQuestion.setRank(eventQuestionForm.getRank());
			if (eventQuestionForm.getType().equals("0")) {
				eventQuestion.setType(ChoiceType.EXCLUSIVE);
			} else {
				eventQuestion.setType(ChoiceType.MULTIPLE);
			}
			eventQuestion.setEvent(giftEvent);
			List<QuestionOption> questionOptions = new ArrayList<>();
			for (int j = 0; j < eventQuestionForm.getQuestionOptionList().length; j++) {
				QuestionOption questionOption = new QuestionOption();
				questionOption.setQuestion(eventQuestion);
				questionOption.setValue(eventQuestionForm.getQuestionOptionList()[j]);
				questionOptions.add(questionOption);
			}
			eventQuestion.setOptions(questionOptions);
			eventQuestions.add(eventQuestion);
		}
		giftEvent.setQuestions(eventQuestions);
		eventService.createGiftEvent(giftEvent);
		return resultData;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/overview")
	public DataTablePage<Event> overview(DataTableParam param) {
		DataTablePage<Event> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		ResultData fetchResponse = eventService.fetchGiftEventByPage(condition, param);
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<Event>) fetchResponse.getData();
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/overview")
	public ModelAndView overview() {
		ModelAndView view = new ModelAndView();
		view.setViewName("backend/event/overview");
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{eventId}")
	public ModelAndView preview(@PathVariable("eventId") String eventId) {
		ModelAndView view = new ModelAndView();

		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
		if (eventId.startsWith("GEV")) {
			ResultData fetchResponse =eventService.fetchGiftEvent(condition);
			if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
				GiftEvent giftEvent=((List<GiftEvent>)fetchResponse.getData()).get(0);
				view.addObject("giftEvent", giftEvent);
			}
			view.setViewName("backend/event/preview");
		}
		
		return view;
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/application")
	public ModelAndView application(){
		ModelAndView view = new ModelAndView();
		Map<String, Object> condition=new HashMap<>();
		condition.put("blockFlag", false);
		ResultData queryResult=eventService.fetchGiftEvent(condition);
		if (queryResult.getResponseCode()==ResponseCode.RESPONSE_OK) {
			view.addObject("giftEvent", ((List<GiftEvent>)queryResult.getData()).get(0));
		}
		view.setViewName("backend/event/application");
		return view;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/application/{eventId}")
	public DataTablePage<EventApplication> application(@PathVariable("eventId") String eventId,DataTableParam param){
		DataTablePage<EventApplication>result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		} 
		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
		condition.put("status", 0);
		ResultData fetchResponse = eventService.fetchEventApplicationByPage(condition, param);
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<EventApplication>) fetchResponse.getData();
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/present")
	public ModelAndView present(){
		ModelAndView view = new ModelAndView();
		Map<String, Object> condition=new HashMap<>();
		condition.put("blockFlag", false);
		ResultData queryResult=eventService.fetchGiftEvent(condition);
		if (queryResult.getResponseCode()==ResponseCode.RESPONSE_OK) {
			view.addObject("giftEvent", ((List<GiftEvent>)queryResult.getData()).get(0));
		}
		view.setViewName("backend/event/present");
		return view;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/present/{eventId}")
	public DataTablePage<EventApplication> present(@PathVariable("eventId") String eventId,DataTableParam param){
		DataTablePage<EventApplication>result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		} 
		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
		condition.put("status", 2);
		ResultData fetchResponse = eventService.fetchEventApplicationByPage(condition, param);
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<EventApplication>) fetchResponse.getData();
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/agree/{applicationId}")
	public ResultData agree(@PathVariable("applicationId") String applicationId) {
		ResultData resultData=new ResultData();
		EventApplication eventApplication=new EventApplication();
		eventApplication.setApplicationId(applicationId);
		eventApplication.setStatus(ApplicationStatus.APPROVED);
		resultData=eventService.updateEventApplication(eventApplication);
		return resultData;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/reject/{applicationId}")
	public ResultData reject(@PathVariable("applicationId") String applicationId) {
		ResultData resultData=new ResultData();
		EventApplication eventApplication=new EventApplication();
		eventApplication.setApplicationId(applicationId);
		eventApplication.setStatus(ApplicationStatus.REJECTED);
		resultData=eventService.updateEventApplication(eventApplication);
		return resultData;
	}
}