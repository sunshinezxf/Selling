package selling.sunshine.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import selling.sunshine.form.SortRule;
import selling.sunshine.model.CustomerOrder;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.PlatformConfig;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 7/8/16.
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET, value = "/test")
    public String testExpress(HttpServletRequest request) {
        String context = request.getSession().getServletContext().getRealPath("/");
        StringBuffer path = new StringBuffer(context);
        path.append(PlatformConfig.getValue("express_template"));
        logger.debug(path.toString());
        File file = new File(path.toString());
        if (!file.exists()) {
            logger.error("文件不存在");
            return "";
        }
        try {
            NPOIFSFileSystem pkg = new NPOIFSFileSystem(file);
            Workbook workbook = new HSSFWorkbook(pkg.getRoot(), true);
            Map<String, Object> condition = new HashMap<>();
            List<Integer> status = new ArrayList<>();
            status.add(1);
            condition.put("status", status);
            List<SortRule> rule = new ArrayList<>();
            rule.add(new SortRule("create_time", "asc"));
            condition.put("sort", rule);
            ResultData fetchResponse = orderService.fetchCustomerOrder(condition);
            if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                return "";
            }
            List<CustomerOrder> list = (List<CustomerOrder>) fetchResponse.getData();
            for (int row = 3, i = 0; i < list.size(); i++, row++) {
                CustomerOrder order = list.get(i);
                Sheet sheet = workbook.getSheetAt(0);
                Row current = sheet.createRow(row);
                Cell senderName = current.createCell(2);
                senderName.setCellValue(PlatformConfig.getValue("sender_name"));
                Cell senderPhone = current.createCell(3);
                senderPhone.setCellValue(PlatformConfig.getValue("sender_phone"));
                Cell senderAddress = current.createCell(7);
                senderAddress.setCellValue(PlatformConfig.getValue("sender_address"));
                Cell receiverName = current.createCell(8);
                receiverName.setCellValue(order.getReceiverName());
                Cell receiverPhone = current.createCell(9);
                receiverPhone.setCellValue(order.getReceiverPhone());
                Cell receiverAddress = current.createCell(13);
                receiverAddress.setCellValue(order.getReceiverAddress());
                Cell goods = current.createCell(14);
                goods.setCellValue(order.getGoods().getName());
                Cell description = current.createCell(22);
                description.setCellValue(order.getQuantity() + "盒");
                Cell orderNo = current.createCell(37);
                orderNo.setCellValue(order.getOrderId());               
            }
            FileOutputStream out = new FileOutputStream(context + "/material/template/test.xls");
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        }
        return "";
    }
}
