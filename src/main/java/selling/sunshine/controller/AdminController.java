package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.model.User;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AdminService;
import selling.sunshine.service.UserService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.Map;

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

    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/admin/admin_management");
        return view;
    }

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
}
