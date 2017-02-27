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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import selling.sunshine.model.BackOperationLog;
import selling.sunshine.service.LogService;
import common.sunshine.pagination.MobilePage;
import common.sunshine.pagination.MobilePageParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.Map;


/**
 * 系统日志相关接口
 * Created by sunshine on 7/15/16.
 */
@RequestMapping("/log")
@RestController
public class LoggerController {
    private Logger logger = LoggerFactory.getLogger(LoggerController.class);

    @Autowired
    private LogService logService;

    /**
     * 跳转到日志查询页面
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/system/log");
        return view;
    }

    /**
     * 根据日期查询系统日志
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/fetch")
    public MobilePage<BackOperationLog> fetchLog(MobilePageParam param){
    	MobilePage<BackOperationLog> result = new MobilePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        if(param.getParams() != null){
	        JSONObject params = JSON.parseObject(param.getParams());
	        String start = (String) params.get("start");
	        String end = (String) params.get("end");
	        String adminInfo = (String) params.get("adminInfo");
	        if(!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)){
	        	condition.put("start", start);
	        	condition.put("end", end);
	        }
	        if(!StringUtils.isEmpty(adminInfo)){
	        	condition.put("adminInfo", adminInfo);
	        }
        }
        ResultData fetchLogResponse = logService.fetchBackOperationLog(condition, param);
        if(fetchLogResponse.getResponseCode() != ResponseCode.RESPONSE_OK){
        	return result;
        }
        result = (MobilePage<BackOperationLog>) fetchLogResponse.getData();
        return result;
    }
}
