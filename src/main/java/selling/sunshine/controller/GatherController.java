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
import selling.sunshine.model.CustomerOrderBill;
import selling.sunshine.model.OrderBill;
import selling.sunshine.service.BillService;
import selling.sunshine.service.GatherService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 7/12/16.
 */
@RequestMapping("/gather")
@RestController
public class GatherController {
    private Logger logger = LoggerFactory.getLogger(GatherController.class);

    @Autowired
    private BillService billService;

    @Autowired
    private GatherService gatherService;

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView gather() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/finance/gather");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public ResultData gather(@Valid TimeRangeForm form, BindingResult result) {
        ResultData data = new ResultData();
        boolean empty = true;
        if (result.hasErrors()) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("start", form.getStart());
        condition.put("end", form.getEnd());
        condition.put("status", 1);
        ResultData queryResponse = billService.fetchOrderBill(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            empty = false;
            List<OrderBill> list = (List<OrderBill>) queryResponse.getData();
            gatherService.produce(list);
        }
        queryResponse = billService.fetchCustomerOrderBill(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            empty = false;
            List<CustomerOrderBill> list = ((List<CustomerOrderBill>) queryResponse.getData());
            gatherService.produce(list);
        }
        if (empty) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }
        return data;
    }
}
