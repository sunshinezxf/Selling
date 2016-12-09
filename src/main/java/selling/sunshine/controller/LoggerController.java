package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.service.LogService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 7/15/16.
 */
@RequestMapping("/log")
@RestController
public class LoggerController {
    private Logger logger = LoggerFactory.getLogger(LoggerController.class);

    @Autowired
    private LogService logService;

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/system/log");
        return view;
    }
//    
//    @RequestMapping(method = RequestMethod.GET, value="/fetch")
//    public ResultData fetchLog()
}
