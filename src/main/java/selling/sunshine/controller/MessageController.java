package selling.sunshine.controller;

import com.alibaba.fastjson.JSON;
import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.agent.support.AgentType;
import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.event.support.ApplicationStatus;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.CustomerService;
import selling.sunshine.service.EventService;
import selling.sunshine.service.MessageService;
import selling.sunshine.vo.customer.CustomerVo;

import java.util.*;

/**
 * 短信接口
 * Created by sunshine on 7/14/16.
 */
@RequestMapping("/message")
@RestController
public class MessageController {
    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private EventService eventService;

    @Autowired
    private CustomerService customerService;

    /**
     * 跳转到短信推送页面
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/send")
    public ModelAndView message() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/message/send");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/customer/send")
    public ModelAndView message2customer() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/message/customer_send");
        return view;
    }

    /**
     * 发送短信给普通代理商
     *
     * @param text
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public ResultData message(String text) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("granted", true);
        condition.put("blockFlag", false);
        condition.put("agentType", AgentType.ORDINARY.getCode());//只查询普通代理商
        ResultData fetchResponse = agentService.fetchAgent(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Agent> list = (List<Agent>) fetchResponse.getData();
            Set<String> mobile = new HashSet<>();
            list.forEach(item -> mobile.add(item.getPhone()));
            ResultData sendResponse = messageService.send(mobile, text + "【云草纲目】");
            result.setResponseCode(sendResponse.getResponseCode());
        }
        return result;
    }

    /**
     * 发送短信给客户
     *
     * @param text
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/customer/send")
    public ResultData message2customer(String text) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = customerService.fetchCustomer(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CustomerVo> list = (List<CustomerVo>) response.getData();
            Set<String> mobile = new HashSet<>();
            list.forEach(item -> mobile.add(item.getPhone()));
            logger.debug("receiver: " + mobile.size() + ", detail: " + JSON.toJSONString(mobile));
            logger.debug("message: " + text);
            response = messageService.send(mobile, text + "【云草纲目】");
            result.setResponseCode(response.getResponseCode());
        }
        return result;
    }

    /**
     * 测试发送短信
     *
     * @param phone
     * @param text
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/preview")
    public ResultData preview(String phone, String text) {
        ResultData result = new ResultData();
        ResultData preview = messageService.preview(phone, text + "【云草纲目】");
        result.setResponseCode(preview.getResponseCode());
        if (preview.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(preview.getDescription());
        }
        return result;
    }

    /**
     * 根据type发送短信给gift event的活动申请者、活动被赠送者、两者都发
     *
     * @param eventId
     * @param type
     * @param text
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/gift/{eventId}/send/{type}")
    public ResultData event(@PathVariable("eventId") String eventId, @PathVariable("type") String type, String text) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("eventId", eventId);
        condition.put("status", ApplicationStatus.APPROVED.getCode());
        ResultData fetchResponse = eventService.fetchEventApplication(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<EventApplication> eventApplications = (List<EventApplication>) fetchResponse.getData();
            Set<String> phone = new HashSet<>();
            if (!StringUtils.isEmpty(type) && type.equals("donor")) {
                for (EventApplication item : eventApplications) {
                    phone.add(item.getDonorPhone());
                }
            }
            if (!StringUtils.isEmpty(type) && type.equals("donee")) {
                for (EventApplication item : eventApplications) {
                    phone.add(item.getDoneePhone());
                }
            }
            if (!StringUtils.isEmpty(type) && type.equals("all")) {
                for (EventApplication item : eventApplications) {
                    phone.add(item.getDonorPhone());
                    phone.add(item.getDoneePhone());
                }
            }
            messageService.send(phone, text + "【云草纲目】");
        }
        return result;
    }


}
