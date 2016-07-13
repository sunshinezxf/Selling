package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.SortRule;
import selling.sunshine.form.TimeRangeForm;
import selling.sunshine.model.CustomerOrder;
import selling.sunshine.model.OrderItem;
import selling.sunshine.service.IndentService;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        condition.put("statusList", status);
        condition.put("blockFalg", false);
        List<SortRule> rule = new ArrayList<>();
        rule.add(new SortRule("create_time", "asc"));
        condition.put("sort", rule);
        ResultData queryResponse = orderService.fetchOrderItem(condition);
        Timestamp createAt = null;
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            OrderItem item = ((List<OrderItem>) queryResponse.getData()).get(0);
            createAt = item.getCreateAt();
        }
        condition.remove("statusList");
        condition.put("status", status);
        queryResponse = orderService.fetchCustomerOrder(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            CustomerOrder item = ((List<CustomerOrder>) queryResponse.getData()).get(0);
            if (createAt == null || createAt.after(item.getCreateAt())) {
                createAt = item.getCreateAt();
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        view.addObject("start", format.format(createAt));
        view.setViewName("/backend/finance/indent");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public ResultData indent(@Valid TimeRangeForm form, BindingResult result) {
        ResultData data = new ResultData();
        if (result.hasErrors()) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("start", form.getStart());
        condition.put("end", form.getEnd());
        condition.put("blockFlag", false);
        List<Integer> status = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        condition.put("statusList", status);
        ResultData queryResponse = orderService.fetchOrderItem(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<OrderItem> list = (List<OrderItem>) queryResponse.getData();
            indentService.produce(list);
        }
        condition.remove("statusList");
        condition.put("status", status);
        queryResponse = orderService.fetchCustomerOrder(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CustomerOrder> list = (List<CustomerOrder>) queryResponse.getData();
            indentService.produce(list);
        }
        String path = IndentController.class.getResource("/").getPath();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") >= 0) {
            path = path.substring(1);
        }

        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/indent";
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(form.getStart().replaceAll("-", ""));
        System.err.println(sb.toString());
        return data;
    }
}
