package selling.sunshine.controller;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.model.CustomerOrder;
import selling.sunshine.model.OrderItem;
import selling.sunshine.model.express.Express;
import selling.sunshine.model.express.Express4Agent;
import selling.sunshine.service.ExpressService;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/express")
@RestController
public class ExpressController {

    private Logger logger = LoggerFactory.getLogger(ExpressController.class);

    @Autowired
    private ExpressService expressService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.POST, value = "/detail/{orderItemId}")
    public ResultData detail(@PathVariable("orderItemId") String orderItemId) {
        ResultData resultData = new ResultData();
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderItemId", orderItemId);
        ResultData queryResponse = expressService.fetchExpress4Agent(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Express4Agent express = ((List<Express4Agent>) queryResponse.getData()).get(0);
            dataMap.put("express", express);
            resultData.setData(dataMap);
        } else {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        return resultData;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/upload")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/express/express_upload");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ModelAndView overview(MultipartHttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        MultipartFile file = request.getFile("excelFile");
        if (file.isEmpty()) {
            view.setViewName("/backend/express/express_upload");
            return view;
        }
        System.out.println(".............test");
        // 创建一个list 用来存储读取的内容
        List<Object> list = new ArrayList<>();
        Workbook rwb = null;
        Cell cell = null;
        // 创建输入流
        InputStream stream;
        try {
            stream = file.getInputStream();
            // 获取Excel文件对象
            rwb = Workbook.getWorkbook(stream);
            // 获取文件的指定工作表 默认的第一个
            Sheet sheet = rwb.getSheet(0);
            // 行数(表头的目录不需要，从1开始)
            for (int i = 0; i < sheet.getRows(); i++) {
                // 创建一个数组 用来存储每一列的值
                String[] str = new String[sheet.getColumns()];
                // 列数
                for (int j = 0; j < sheet.getColumns(); j++) {
                    // 获取第i行，第j列的值
                    cell = sheet.getCell(j, i);
                    str[j] = cell.getContents();
                }
                // 把刚获取的列存入list
                list.add(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Express> expressList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        for (int i = 1; i < list.size(); i++) {
            String[] str = (String[]) list.get(i);
            if (str.length < 7) {
                view.setViewName("/backend/express/express_upload");
                return view;
            }
            Express express = new Express("尚未设置", str[0], str[1], str[2], str[3], str[4], str[5], str[6]);
            if (str.length >= 8 && !StringUtils.isEmpty(str[7])) {
                condition.clear();
                condition.put("orderItemId", str[7]);
                if (orderService.fetchOrderItem(condition).getResponseCode() == ResponseCode.RESPONSE_OK) {
                    OrderItem item = ((List<OrderItem>) orderService.fetchOrderItem(condition).getData()).get(0);
                    express.setLinkId(item.getOrderItemId());
                }
            } else if (str.length >= 9 && !StringUtils.isEmpty(str[8])) {
                condition.clear();
                condition.put("orderId", str[8]);
                ResultData fetchResponse = orderService.fetchCustomerOrder(condition);
                if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    CustomerOrder order = ((List<CustomerOrder>) fetchResponse.getData()).get(0);
                    express.setLinkId(order.getOrderId());
                }
            }
            express.setExpressId("expressNumber" + (i - 1));
            expressList.add(express);
        }
        view.addObject("expressList", expressList);
        view.setViewName("/backend/order/express");
        //view.setViewName("redirect:/agent/express");
        return view;
    }

}
