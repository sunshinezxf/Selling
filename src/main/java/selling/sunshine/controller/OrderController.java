package selling.sunshine.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by sunshine on 4/11/16.
 */
@RequestMapping("/order")
@RestController
public class OrderController {
    @RequestMapping(method = RequestMethod.GET, value = "/check")
    public ModelAndView handle() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/check");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/overview");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/express")
    public ModelAndView express() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/express");
        return view;
    }
}
