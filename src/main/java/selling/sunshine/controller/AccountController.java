package selling.sunshine.controller;

import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Charge;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.model.DepositBill;
import selling.sunshine.model.User;
import selling.sunshine.service.ToolService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sunshine on 4/14/16.
 */
@RequestMapping("/account")
@RestController
public class AccountController {

    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private ToolService toolService;

    @RequestMapping(method = RequestMethod.GET, value = "/deposit")
    public ModelAndView deposit() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/account/recharge");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/deposit")
    public Charge deposit(HttpServletRequest request) {
        Charge charge = new Charge();
        JSONObject params = toolService.getParams(request);
        logger.debug(JSONObject.toJSONString(params));
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();

        return charge;
    }
}
