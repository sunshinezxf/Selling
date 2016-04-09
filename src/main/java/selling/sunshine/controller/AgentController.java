package selling.sunshine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.service.AgentService;

/**
 * Created by sunshine on 4/8/16.
 */
@RequestMapping("/agent")
@RestController
public class AgentController {
    @Autowired
    private AgentService agentService;

    @RequestMapping(method = RequestMethod.GET, value = "/index")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();

        view.setViewName("/agent/index");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/register")
    public ModelAndView register() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/register");
        return view;
    }
}
