package selling.sunshine.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.AdminForm;
import selling.sunshine.form.AdminLoginForm;
import selling.sunshine.model.Admin;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.model.Role;
import selling.sunshine.model.User;
import selling.sunshine.model.sum.OrderMonth;
import selling.sunshine.model.sum.TopThreeAgent;
import selling.sunshine.service.AdminService;
import selling.sunshine.service.LogService;
import selling.sunshine.service.RoleService;
import selling.sunshine.service.StatisticService;
import selling.sunshine.service.ToolService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private ToolService toolService;
    
    @Autowired
    private LogService logService;
    
    @Autowired
    private StatisticService statisticService;

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
    public ModelAndView login(@Valid AdminLoginForm form, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/login");
            return view;
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated() && subject.getPrincipal() != null) {
                subject.logout();
            }
            subject.login(new UsernamePasswordToken(form.getUsername(), form.getPassword()));
            User user = (User) subject.getPrincipal();
            HttpSession session = request.getSession();
            session.setAttribute("role", user.getRole().getName());
            switch (user.getRole().getName()) {
                case "admin":
                    view.setViewName("redirect:/dashboard");
                    return view;
                case "auditor":
                    view.setViewName("redirect:/agent/check");
                    return view;
                case "express":
                    view.setViewName("redirect:/order/check");
                    return view;
                case "finance":
                    view.setViewName("redirect:/withdraw/check");
                    return view;
            }
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
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData fetchResponse = roleService.queryRole(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Role> list = (List<Role>) fetchResponse.getData();
            view.addObject("roles", list);
        }
        view.setViewName("/backend/admin/admin_register");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ModelAndView register(@Valid AdminForm form, BindingResult result,HttpServletRequest request) {
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
            Role role = new Role();
            role.setRoleId(form.getRole());
            Admin admin = new Admin(form.getUsername(), form.getPassword());
            ResultData resultData = adminService.createAdmin(admin, role);
            if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                Subject subject = SecurityUtils.getSubject();
                User user = (User) subject.getPrincipal();
                if (user == null) {
                	view.setViewName("redirect:/commodity/create");
                    return view;
                }
                Admin targetAdmin = user.getAdmin();
                BackOperationLog backOperationLog = new BackOperationLog(
                		targetAdmin.getUsername(), toolService.getIP(request) ,"管理员" + targetAdmin.getUsername() + "新授权了一个管理员账号："+form.getUsername());
                logService.createbackOperationLog(backOperationLog);
                view.setViewName("redirect:/admin/overview");
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

    @RequestMapping(method = RequestMethod.GET, value = "/dashboard")
    public ModelAndView dashboard() {
        ModelAndView view = new ModelAndView();
        ResultData resultData = new ResultData();
        resultData=statisticService.orderMonth();
        if (resultData.getResponseCode()==ResponseCode.RESPONSE_OK) {
        	OrderMonth orderMonth=((List<OrderMonth>)resultData.getData()).get(0);
        	orderMonth.setPrice(((int)(orderMonth.getPrice()*100)*1.0/100));
        	orderMonth.setTotalPrice(((int)(orderMonth.getTotalPrice()*100)*1.0/100));
        	view.addObject("orderMonth", orderMonth);
		}else{
			OrderMonth orderMonth=new OrderMonth();
        	view.addObject("orderMonth", orderMonth);
		}
        resultData = statisticService.topThreeAgent();
        if (resultData.getResponseCode()==ResponseCode.RESPONSE_OK) {
        	List<Object> list=(List<Object>)resultData.getData();
        	List<TopThreeAgent> monthList=(List<TopThreeAgent>)list.get(0);
        	List<TopThreeAgent> allList=(List<TopThreeAgent>)list.get(1);
        	if (monthList.size()!=0) {
        		view.addObject("monthList", monthList);
			}
        	view.addObject("allList", allList);
		}
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

    @RequestMapping(method = RequestMethod.GET, value = "/log")
    public ModelAndView log() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/system/log");
        return view;
    }
}
