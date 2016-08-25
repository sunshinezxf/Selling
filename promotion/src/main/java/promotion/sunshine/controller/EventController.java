package promotion.sunshine.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import common.sunshine.model.selling.event.Event;
import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.event.GiftEvent;
import common.sunshine.model.selling.event.QuestionAnswer;
import common.sunshine.model.selling.event.QuestionOption;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import promotion.sunshine.form.EventApplicationForm;
import promotion.sunshine.service.EventService;
import promotion.sunshine.utils.Prompt;
import promotion.sunshine.utils.PromptCode;

/**
 * Created by sunshine on 8/22/16.
 */
@RestController
@RequestMapping("/event")
public class EventController {
	private Logger logger = LoggerFactory.getLogger(EventController.class);

	@Autowired
	private EventService eventService;

	/**
	 * 活动通用页面
	 * 
	 * @param eventName
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{eventName}")
	public ModelAndView view(@PathVariable("eventName") String eventName, HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		HttpSession session = request.getSession();
		session.setAttribute("openId", "123456");
		if(session.getAttribute("openId") == null || ((String)session.getAttribute("openId")).equals("")){
			Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "超时,请重新从订阅号菜单进入活动", "");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/event/prompt");
			return view;
		}
		/*
		 * 先查询用户是否已经填过表单，然后查询活动是否结束或未开始
		 */
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("donorWechat", (String) session.getAttribute("openId"));
		condition.put("blockFlag", false);
		ResultData fetchEventApplicationResponse = eventService.fetchEventApplication(condition);
		if(fetchEventApplicationResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
			Prompt prompt = new Prompt(PromptCode.SUCCESS, "提示", "您已经提交过申请", "");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/event/prompt");
			return view;
		}
		
		condition.clear();
		condition.put("nickname", eventName);
		condition.put("blockFlag", false);
		ResultData fetchEventResponse = eventService.fetchGiftEvent(condition);
		if (fetchEventResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
			Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "未找到该活动", "");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/event/prompt");
			return view;
		}
		GiftEvent event = ((List<GiftEvent>) fetchEventResponse.getData()).get(0);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(now.before(event.getStart())){
			Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "活动尚未开始", "");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/event/prompt");
			return view;
		}
		if(now.before(event.getEnd())){
			Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "活动已结束", "");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/event/prompt");
			return view;
		}
		view.addObject("event", event);
		view.setViewName("/customer/event/apply");
		return view;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/giftapplication")
    public ModelAndView giftApplication(@Valid EventApplicationForm form, BindingResult result, HttpServletRequest request){
    	ModelAndView view = new ModelAndView();
    	HttpSession session = request.getSession();
		if(session.getAttribute("openId") == null || ((String)session.getAttribute("openId")).equals("")){
			Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "操作超时，请重新进入活动", "");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/event/prompt");
			return view;
		}
    	EventApplication eventApplication = new EventApplication(form.getDonor_name(),form.getDonor_phone(),form.getDonee_name(),form.getDonee_phone(),form.getDonee_gender(),form.getDonee_address(),form.getRelation(),form.getWishes(), (String)session.getAttribute("openId"));
    	ResultData insertEventApplicationResponse = eventService.insertEventApplication(eventApplication);
    	if(insertEventApplicationResponse.getResponseCode() != ResponseCode.RESPONSE_OK){
    		Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "未找到该活动", "");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/event/prompt");
    		return view;
    	}
    	Map<String, Object> condition = new HashMap<String, Object>();
    	String[] optionIds = form.getOptionId();
    	eventApplication = (EventApplication) insertEventApplicationResponse.getData();
    	for(String optionId : optionIds){
    		condition.put("optionId", optionId);
    		condition.put("blockFlag", false);
    		ResultData fetchOption = eventService.fetchQuestionOption(condition);
    		if(fetchOption.getResponseCode() == ResponseCode.RESPONSE_OK){
    			QuestionOption questionOption= ((List<QuestionOption>)fetchOption.getData()).get(0);
    			QuestionAnswer questionAnswer = new QuestionAnswer(eventApplication, questionOption.getQuestion().getContent(), questionOption.getValue(), questionOption.getQuestion().getRank());
    			eventService.insertQuestionAnswer(questionAnswer);
    		}
    	}
    	Prompt prompt = new Prompt(PromptCode.SUCCESS, "提示", "申请成功", "");//!!!!!请加上跳转URL
        view.addObject("prompt", prompt);
        view.setViewName("/customer/event/prompt");
    	return view;
    }
	
	@RequestMapping(method = RequestMethod.GET, value="/giftapplication")
	public ModelAndView giftApplication(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		HttpSession session = request.getSession();
		if(session.getAttribute("openId") == null || ((String)session.getAttribute("openId")).equals("")){
			Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "操作超时，请重新进入活动", "");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/event/prompt");
			return view;
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("donorWechat", (String) session.getAttribute("openId"));
		condition.put("blockFlag", false);
		ResultData fetchEventApplicationResponse = eventService.fetchEventApplication(condition);
		if(fetchEventApplicationResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
			Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "您还没有参加该活动", "");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/event/prompt");
			return view;
		}
		EventApplication eventApplication = ((List<EventApplication>)fetchEventApplicationResponse.getData()).get(0);
		view.addObject("eventApplication", eventApplication);
		//活动结果查询页面
		return view;
	}
}
