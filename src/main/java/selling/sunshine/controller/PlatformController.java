package selling.sunshine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.AdminForm;
import selling.sunshine.model.Admin;
import selling.sunshine.service.AdminService;
import selling.sunshine.utils.ResultData;

import javax.validation.Valid;

/**
 * Created by sunshine on 4/10/16.
 */
@RequestMapping("/")
@RestController
public class PlatformController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        view.setViewName("redirect:/login");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public ModelAndView login() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/login");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ModelAndView login(@Valid AdminForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/login");
        }
        ResultData loginResponse = adminService.login(new Admin(form.getUsername(), form.getPassword()));
        view.setViewName("redirect:/dashboard");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dashboard")
    public ModelAndView dashboard() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/dashboard");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    public ModelAndView logout() {
        ModelAndView view = new ModelAndView();
        view.setViewName("redirect:/login");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/navigate")
    public ModelAndView navigate() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/navigate");
        return view;
    }
}
