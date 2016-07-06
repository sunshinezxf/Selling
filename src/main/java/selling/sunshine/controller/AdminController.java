package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import selling.sunshine.form.AdminLoginForm;
import selling.sunshine.model.Admin;
import selling.sunshine.model.User;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AdminService;
import selling.sunshine.service.UserService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

/**
 * Created by sunshine on 7/5/16.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/admin/admin_management");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public DataTablePage<User> overview(DataTableParam param) {
        DataTablePage<User> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("isAdmin", true);
        ResultData fetchResponse = userService.fetchUser(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<User>) fetchResponse.getData();
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete/{userId}")
    public ResultData deleteAdmin(@PathVariable("userId") String userId) {
        ResultData response = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("userId", userId);
        User user=(User)userService.fetchUser(condition).getData();
        if ("ROL00000001".equals(user.getRole().getRoleId())) {
			condition.clear();
			condition.put("roleId", "ROL00000001");
			DataTableParam param=new DataTableParam();
			param.setiDisplayStart(0);
			param.setiDisplayLength(10);
			List<User> userList=((DataTablePage<User>)userService.fetchUser(condition,param).getData()).getData();
			if (userList.size()>1) {
				response = adminService.deleteAdmin(user.getAdmin());
		        return response;
			}else {
			       //当只有一个admin时不允许删除
				  response.setResponseCode(ResponseCode.RESPONSE_NULL);
		          return response;
			}
		}else {
	        response = adminService.deleteAdmin(user.getAdmin());
	        return response;
		}

    }
    

    @RequestMapping(method = RequestMethod.POST, value = "/modify/{adminId}")
    public ModelAndView updateAdmin(@PathVariable("adminId") String adminId, @Valid AdminLoginForm adminLoginForm, BindingResult result) {
        ResultData response = new ResultData();
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/admin/overview");
            return view;
        }
        Admin admin = new Admin(adminLoginForm.getUsername(), adminLoginForm.getPassword());
        admin.setAdminId(adminId);
        response = adminService.updateAdmin(admin);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
        	view.setViewName("redirect:/admin/overview");
            return view;
        } else {
        	view.setViewName("redirect:/admin/overview");
            return view;
        }
    }
}
