package selling.sunshine.controller;

import org.apache.shiro.SecurityUtils;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import selling.sunshine.form.AdminForm;
import selling.sunshine.model.Admin;
import selling.sunshine.service.AdminService;
import selling.sunshine.utils.Prompt;
import selling.sunshine.utils.PromptCode;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.servlet.http.HttpSession;
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
			return view;
		}
		try {
			Subject subject = SecurityUtils.getSubject();
			if (subject.isAuthenticated()) {
				view.setViewName("redirect:/dashboard");
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
			Admin admin = new Admin(form.getUsername(), form.getPassword());
			ResultData resultData = adminService.createAdmin(admin);
			if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
				view.setViewName("redirect:/dashboard");
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
		view.setViewName("/backend/dashboard");
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/logout")
	public ModelAndView logout(HttpSession session) {
		ModelAndView view = new ModelAndView();
		session.removeAttribute("current");
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
