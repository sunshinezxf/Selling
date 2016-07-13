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
import selling.sunshine.model.CustomerOrder;
import selling.sunshine.model.OrderItem;
import selling.sunshine.service.IndentService;
import selling.sunshine.service.OrderService;
import selling.sunshine.service.impl.IndentServiceImpl;
import selling.sunshine.utils.DateUtils;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import selling.sunshine.utils.ZipCompressor;

import javax.validation.Valid;

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
        if(os.indexOf("windows") >= 0){
        	path = path.substring(1);
        }
        
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/indent";
        DateUtils dateUtils=new DateUtils();
        dateUtils.process(form.getStart(), form.getEnd());
        List<String> dateList=dateUtils.getDateList();
        List<String> pathList=new ArrayList<String>();
        dateList.forEach((date)->{
        	StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(date.replaceAll("-", ""));
        	pathList.add(sb.toString());
        });
        String zipName=IDGenerator.generate("Indent");
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(zipName+".zip");
        ZipCompressor zipCompressor=new ZipCompressor(sb.toString());
        zipCompressor.compress(pathList);
        data.setData((new StringBuffer(directory).append("/").append(zipName+".zip")).toString());
        return data;
    }
}
