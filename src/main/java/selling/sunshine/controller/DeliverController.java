package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.TimeRangeForm;
import selling.sunshine.model.express.Express;
import selling.sunshine.service.DeliverService;
import selling.sunshine.service.ExpressService;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 7/13/16.
 */
@RequestMapping("/deliver")
@RestController
public class DeliverController {
    private Logger logger = LoggerFactory.getLogger(DeliverController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private DeliverService deliverService;

    @Autowired
    private ExpressService expressService;

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView deliver() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/finance/deliver");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public ResultData deliver(@Valid TimeRangeForm form, BindingResult result) {
        ResultData data = new ResultData();
        if (result.hasErrors()) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("start", form.getStart());
        condition.put("end", form.getEnd());
        ResultData queryResponse = expressService.fetchExpress(condition);
        if (queryResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }
        List<Express> list = (List<Express>) queryResponse.getData();
        ResultData produceResponse = deliverService.produce(list);
        if (produceResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }
        return data;
    }
}
