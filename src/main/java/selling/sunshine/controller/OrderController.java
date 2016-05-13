package selling.sunshine.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.model.Order;
import selling.sunshine.model.OrderStatus;
import selling.sunshine.pagination.MobilePage;
import selling.sunshine.pagination.MobilePageParam;

import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping(method = RequestMethod.POST, value = "/check")
    public MobilePage<Order> handle(MobilePageParam param) {
        MobilePage<Order> result = new MobilePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("status", OrderStatus.PAYED);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/overview");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public MobilePage<Order> overview(MobilePageParam param) {
        MobilePage<Order> result = new MobilePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/express")
    public ModelAndView express() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/express");
        return view;
    }
}
