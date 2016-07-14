package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 7/14/16.
 */
@RequestMapping("/message")
@RestController
public class MessageController {
    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/send")
    public ModelAndView message() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/message/send");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public ResultData message(String text) {
        ResultData result = new ResultData();

        return result;
    }
}
