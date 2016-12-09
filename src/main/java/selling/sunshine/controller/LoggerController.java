package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.service.LogService;
import common.sunshine.model.selling.order.Order;
import common.sunshine.pagination.MobilePage;
import common.sunshine.pagination.MobilePageParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/fetch")
    public MobilePage<BackOperationLog> fetchLog(MobilePageParam param){
    	MobilePage<BackOperationLog> result = new MobilePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        Map params = param.getParams();
        String start = (String) params.get("start");
        String end = (String) params.get("end");
        String adminId = (String) params.get("adminId");
        if(!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)){
        	condition.put("start", start);
        	condition.put("end", end);
        }
        if(!StringUtils.isEmpty(adminId)){
        	condition.put("adminId", adminId);
        }
        ResultData fetchLogResponse = logService.fetchBackOperationLog(condition, param);
        if(fetchLogResponse.getResponseCode() != ResponseCode.RESPONSE_OK){
        	return result;
        }
        result = (MobilePage<BackOperationLog>) fetchLogResponse.getData();
        return result;
    }
}
