package selling.sunshine.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by sunshine on 4/14/16.
 */
@RequestMapping("/account")
@RestController
public class AccountController {

    @RequestMapping(method = RequestMethod.GET, value = "/deposit")
    public ModelAndView deposit() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/account/recharge");
        return view;
    }
}
