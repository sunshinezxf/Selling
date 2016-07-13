package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.TimeRangeForm;
import selling.sunshine.utils.ResultData;

import javax.validation.Valid;

/**
 * Created by sunshine on 7/12/16.
 */
@RequestMapping("/gather")
@RestController
public class GatherController {
    private Logger logger = LoggerFactory.getLogger(GatherController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView gather() {
        ModelAndView view = new ModelAndView();

        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public ResultData gather(@Valid TimeRangeForm form, BindingResult result) {
        ResultData data = new ResultData();

        return data;
    }
}
