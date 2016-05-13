package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by sunshine on 5/13/16.
 */
@RequestMapping("/shipment")
@RestController
public class ShipmentController {

    private Logger logger = LoggerFactory.getLogger(ShipmentController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public ModelAndView config() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/shipment/config");
        return view;
    }
}
