package selling.sunshine.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.ShipConfigForm;
import common.sunshine.model.selling.admin.Admin;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.model.ShipConfig;
import common.sunshine.model.selling.user.User;
import selling.sunshine.service.LogService;
import selling.sunshine.service.ShipmentService;
import selling.sunshine.service.ToolService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已废弃接口
 * Created by sunshine on 5/13/16.
 */
@RequestMapping("/shipment")
@RestController
public class ShipmentController {

    private Logger logger = LoggerFactory.getLogger(ShipmentController.class);

    @Autowired
    private ShipmentService shipmentService;
    
    @Autowired
    private ToolService toolService;
    
    @Autowired
    private LogService logService;

    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public ModelAndView config() {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        ResultData fetchResponse = shipmentService.fetchShipmentConfig(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            view.addObject("config", JSONObject.toJSONString(fetchResponse.getData()));
            view.setViewName("/backend/shipment/config");
            return view;
        }
        view.setViewName("/backend/shipment/config");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/config")
    public ResultData config(@Valid ShipConfigForm form, BindingResult result,HttpServletRequest request) {
        ResultData data = new ResultData();
        if (result.hasErrors()) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }
        JSONArray dates = JSONArray.parseArray(form.getDate());
        List<ShipConfig> list = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            String item = String.valueOf(dates.get(i));
            list.add(new ShipConfig(Integer.parseInt(item)));
        }
        ResultData createResponse = shipmentService.createShipmentConfig(list);
        if (createResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            data.setResponseCode(createResponse.getResponseCode());
            data.setDescription(createResponse.getDescription());
        } else {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            if (user == null) {
            	data.setResponseCode(ResponseCode.RESPONSE_ERROR);
                return data;
            }
            Admin admin = user.getAdmin();
            BackOperationLog backOperationLog = new BackOperationLog(
                    admin.getUsername(), toolService.getIP(request) ,"管理员" + admin.getUsername() + "配置了发货");
            logService.createbackOperationLog(backOperationLog);
            data.setData(createResponse.getData());
        }
        return data;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData list() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        ResultData fetchResponse = shipmentService.fetchShipmentConfig(condition);
        result.setResponseCode(fetchResponse.getResponseCode());
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<ShipConfig>) fetchResponse.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(fetchResponse.getData());
            }
        } else {
            result.setDescription(fetchResponse.getDescription());
        }
        return result;
    }
}
