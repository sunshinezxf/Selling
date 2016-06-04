package selling.sunshine.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.AdminForm;
import selling.sunshine.model.Admin;
import selling.sunshine.service.AdminService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/10/16.
 */
@RequestMapping("/")
@RestController
public class PlatformController {

    private Logger logger = LoggerFactory.getLogger(PlatformController.class);

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
            return view;
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated()) {
                view.setViewName("redirect:/login");
                return view;
            }
            subject.login(new UsernamePasswordToken(form.getUsername(), form
                    .getPassword()));
        } catch (Exception e) {
            view.setViewName("redirect:/login");
            return view;
        }
        view.setViewName("redirect:/dashboard");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/register")
    public ModelAndView register() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/admin/admin_register");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ModelAndView register(@Valid AdminForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();

        if (result.hasErrors()) {
            view.setViewName("redirect:/register");
            return view;
        }
        try {
            Map<String, Object> condition = new HashMap<>();
            condition.put("username", form.getUsername());
            ResultData queryResult = adminService.fetchAdmin(condition);
            if (((List<Admin>) queryResult.getData()).size() != 0) {
                view.setViewName("redirect:/register");
                return view;
            }
            Admin admin = new Admin(form.getUsername(), form.getPassword());
            ResultData resultData = adminService.createAdmin(admin);
            if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                view.setViewName("redirect:/manage");
                return view;
            } else {
                view.setViewName("redirect:/register");
                return view;
            }
        } catch (Exception e) {
            view.setViewName("redirect:/register");
            return view;
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/{adminId}")
    public ModelAndView fetchAdmin(@PathVariable("adminId") String adminId) {
        ModelAndView view = new ModelAndView();
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("adminId", adminId);
        result = adminService.fetchAdmin(condition);
        if (result.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            view.setViewName("/backend/admin/admin_management");
            return view;
        }
        Admin admin = ((List<Admin>) result.getData()).get(0);
        view.addObject("admin", admin);
        view.setViewName("/backend/admin/admin_update");
        return view;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/modify/{adminId}")
    public ModelAndView updateAdmin(@PathVariable("adminId") String adminId, @Valid AdminForm adminForm, BindingResult result) {
        ResultData response = new ResultData();
        ModelAndView view = new ModelAndView();


        if (result.hasErrors()) {
            view.setViewName("redirect:/manage");
            return view;
        }
        Admin admin = new Admin(adminForm.getUsername(), adminForm.getPassword());
        admin.setAdminId(adminId);
        response = adminService.updateAdmin(admin);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/manage");
            return view;
        } else {
            view.setViewName("redirect:/manage");
            return view;
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete/{adminId}")
    public ResultData deleteAdmin(@PathVariable("adminId") String adminId) {
        ResultData response = new ResultData();
        ModelAndView view = new ModelAndView();

        Map<String, Object> condition = new HashMap<>();
        response = adminService.fetchAdmin(condition);
        //当只有一个admin时不允许删除
        if (((List<Admin>) response.getData()).size() == 1) {
            response.setResponseCode(ResponseCode.RESPONSE_NULL);
            ;
            return response;
        }

        Admin admin = new Admin();
        admin.setAdminId(adminId);
        response = adminService.deleteAdmin(admin);
        return response;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/admin/overview")
    public ResultData overview() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        result = adminService.fetchAdmin(condition);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/manage")
    public ModelAndView manage() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/admin/admin_management");
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
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
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
