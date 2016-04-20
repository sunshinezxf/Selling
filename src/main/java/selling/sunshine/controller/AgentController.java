package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.AgentForm;
import selling.sunshine.model.Agent;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AgentService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
@RequestMapping("/agent")
@RestController
public class AgentController {
    private Logger logger = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private AgentService agentService;

    @RequestMapping(method = RequestMethod.GET, value = "/me/index")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();

        view.setViewName("/agent/me/index");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/place")
    public ModelAndView placeOrder() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/order/place");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/manage")
    public ModelAndView manageOrder() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/order/manage");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/customer/manage")
    public ModelAndView manageCustomer() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/customer/manage");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/account/info")
    public ModelAndView info() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/account/info");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statement")
    public ModelAndView statement() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/account/statement");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/register")
    public ModelAndView register() {
        ModelAndView view = new ModelAndView();

        view.setViewName("/agent/register");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ModelAndView register(@Valid AgentForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/agent/register");
            return view;
        }
        try {
            Agent agent = new Agent(form.getName(), form.getGender(), form.getPhone(), form.getAddress(), form.getWechat());
            ResultData createResponse = agentService.createAgent(agent);
            if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                view.setViewName("redirect:/agent/prompt");
                return view;
            } else {
                view.setViewName("redirect:/agent/register");
                return view;
            }
        } catch (Exception e) {
            view.setViewName("redirect:/agent/register");
            return view;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/prompt")
    public ModelAndView prompt() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/prompt");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/check")
    public ModelAndView check() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/agent/check");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/check")
    public DataTablePage<Agent> check(DataTableParam param, HttpServletRequest request) {
        DataTablePage<Agent> result = new DataTablePage<Agent>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("paid", 0);
        ResultData fetchResponse = agentService.fetchAgent(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Agent>) fetchResponse.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/agent/overview");
        return view;
    }
}
