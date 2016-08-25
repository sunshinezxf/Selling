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
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import promotion.sunshine.form.EventForm;
import promotion.sunshine.service.EventService;

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
	@RequestMapping(method = RequestMethod.GET, value = "/{eventName}/view")
	public ModelAndView view(@PathVariable("eventName") String eventName, HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		HttpSession session = request.getSession();
		if(session.getAttribute("openId") == null || ((String)session.getAttribute("openId")).equals("")){
			//需要一个错误页面，提示超时
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
			//您已参加过该活动页面
			return view;
		}
		
		condition.clear();
		condition.put("nickname", eventName);
		condition.put("blockFlag", false);
		ResultData fetchEventResponse = eventService.fetchGiftEvent(condition);
		if (fetchEventResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
			//未找到该活动页面
			return view;
		}
		Event event = ((List<Event>) fetchEventResponse.getData()).get(0);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(now.before(event.getStart())){
			//活动未开始页面
			return view;
		}
		if(now.before(event.getEnd())){
			//活动已结束页面
			return view;
		}
		view.addObject("event", event);
		// 需要一个活动页面
		return view;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/giftapplication")
    public ModelAndView giftApplication(@Valid EventForm form, BindingResult result, HttpServletRequest request){
    	ModelAndView view = new ModelAndView();
    	HttpSession session = request.getSession();
		if(session.getAttribute("openId") == null || ((String)session.getAttribute("openId")).equals("")){
			//需要一个错误页面，提示超时
			return view;
		}
    	EventApplication eventApplication = new EventApplication(form.getDonor_name(),form.getDonor_phone(),form.getDonee_name(),form.getDonee_phone(),form.getDonee_gender(),form.getDonee_address(),form.getRelation(),form.getWishes(), (String)session.getAttribute("openId"));
    	ResultData insertEventApplicationResponse = eventService.insertEventApplication(eventApplication);
    	if(insertEventApplicationResponse.getResponseCode() != ResponseCode.RESPONSE_OK){
    		//错误页面
    		return view;
    	}
    	//正常Prompt页面
    	return view;
    }
	
	@RequestMapping(method = RequestMethod.GET, value="/giftapplication")
	public ModelAndView giftApplication(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		HttpSession session = request.getSession();
		if(session.getAttribute("openId") == null || ((String)session.getAttribute("openId")).equals("")){
			//需要一个错误页面，提示超时
			return view;
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("donorWechat", (String) session.getAttribute("openId"));
		condition.put("blockFlag", false);
		ResultData fetchEventApplicationResponse = eventService.fetchEventApplication(condition);
		if(fetchEventApplicationResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
			//您还没有参加该活动
			return view;
		}
		EventApplication eventApplication = ((List<EventApplication>)fetchEventApplicationResponse.getData()).get(0);
		view.addObject("eventApplication", eventApplication);
		//活动结果查询页面
		return view;
	}
}
