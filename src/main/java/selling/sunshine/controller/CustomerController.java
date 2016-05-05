package selling.sunshine.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 4/11/16.
 */
@RequestMapping("/customer")
@RestController
public class CustomerController {

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/customer/overview");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public ResultData addCustomer() {
        ResultData result = new ResultData();

        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/modify")
    public ResultData updateCustomer() {
        ResultData result = new ResultData();

        return result;
    }
}
