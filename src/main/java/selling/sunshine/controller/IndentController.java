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
import selling.sunshine.model.OrderItem;
import selling.sunshine.service.IndentService;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by sunshine on 7/7/16.
 */
@RestController
@RequestMapping("/indent")
public class IndentController {
    private Logger logger = LoggerFactory.getLogger(IndentController.class);

    @Autowired
    private IndentService indentService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView indent() {
        ModelAndView view = new ModelAndView();
        indentService.produce();
        view.setViewName("/backend/finance/indent");
        return view;
    }

    public ResultData indent(@Valid TimeRangeForm form, BindingResult result) {
        ResultData data = new ResultData();
        if (result.hasErrors()) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("start", form.getStart());
        condition.put("end", form.getEnd());
        List<Integer> status = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        condition.put("statusList", status);
        ResultData queryResponse = orderService.fetchOrder(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<OrderItem> list = (List<OrderItem>)queryResponse.getData();
        }
        return data;
    }
}
