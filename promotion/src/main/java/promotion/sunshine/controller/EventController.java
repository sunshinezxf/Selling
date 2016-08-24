package promotion.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping(method = RequestMethod.GET, value = "/{eventName}/")
    public ModelAndView view(@PathVariable("eventName") String eventName) {
        ModelAndView view = new ModelAndView();
        
        return view;
    }
}
