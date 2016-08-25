package selling.sunshine.controller;

import javax.servlet.http.HttpSession;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import common.sunshine.utils.ResultData;
import selling.sunshine.form.GiftEventForm;


/**
 * Created by sunshine on 8/23/16.
 */
@RestController
@RequestMapping("/event")
public class EventController {
    private Logger logger = LoggerFactory.getLogger(EventController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/create")
    public ModelAndView create() {
        ModelAndView view = new ModelAndView();
        view.setViewName("backend/event/create");
        return view;
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData create(@RequestBody GiftEventForm form, HttpSession session) {
    	ResultData resultData=new ResultData();
    	
    	System.err.println(form.getQuestionList().length);
    	return resultData;
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
        return view;
    }
}
