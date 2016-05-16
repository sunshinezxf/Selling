package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.ShipConfigForm;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.validation.Valid;

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

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/config")
    public ResultData config(@Valid ShipConfigForm form, BindingResult result) {
        ResultData data = new ResultData();
        if (result.hasErrors()) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }

        return data;
    }
}
