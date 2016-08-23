package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.model.Agent;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.MessageService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.*;

/**
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

    @RequestMapping(method = RequestMethod.GET, value = "/send")
    public ModelAndView message() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/message/send");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public ResultData message(String text) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("granted", true);
        condition.put("blockFlag", false);
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
}
