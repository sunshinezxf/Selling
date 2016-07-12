package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.service.IndentService;

/**
 * Created by sunshine on 7/7/16.
 */
@RestController
@RequestMapping("/indent")
public class IndentController {
    private Logger logger = LoggerFactory.getLogger(IndentController.class);

    @Autowired
    private IndentService indentService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView indent() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/finance/indent");
        return view;
    }
}
