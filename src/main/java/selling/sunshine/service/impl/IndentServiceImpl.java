package selling.sunshine.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.model.OrderItem;
import selling.sunshine.service.IndentService;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.PlatformConfig;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import selling.sunshine.utils.WorkBookUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sunshine on 7/7/16.
 */
@Service
public class IndentServiceImpl implements IndentService {
    private Logger logger = LoggerFactory.getLogger(IndentServiceImpl.class);

    @Autowired
    private OrderService orderService;

    @Override
    public ResultData generateIndent() {
        ResultData result = new ResultData();
        try {
            Workbook workbook = WorkbookFactory.create(new File(PlatformConfig.getValue("indent_template")));
        } catch (Exception e) {
            logger.error(e.getMessage());

        }
        return result;
    }

    @Override
    public ResultData produce() {
        ResultData result = new ResultData();
        String path = IndentServiceImpl.class.getResource("/").getPath();
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/indent";
        Calendar current = Calendar.getInstance();
        current.add(Calendar.DATE, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String time = format.format(current.getTime());
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(time);
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        Workbook template = WorkBookUtil.getIndentTemplate();
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(2);
        condition.put("statusList", status);
        condition.put("daily", true);
        ResultData fetchResponse = orderService.fetchOrderItem(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<OrderItem> list = (List<OrderItem>) fetchResponse.getData();
            list.forEach(item -> {
                Workbook temp = produce(template, item);
                try {
                    FileOutputStream out = new FileOutputStream(file + "/" + item.getOrderItemId() + ".xlsx");
                    temp.write(out);
                    out.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            });
        }
        return result;
    }

    private Workbook produce(Workbook template, OrderItem item) {
        Sheet sheet = template.getSheetAt(0);
        Row order = sheet.getRow(1);
        Cell orderNo = order.getCell(1);
        orderNo.setCellValue(item.getOrderItemId());
        Cell orderDate = order.getCell(5);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        orderDate.setCellValue(format.format(item.getCreateAt()));
        Row provider = sheet.getRow(2);
        Cell providerName = provider.getCell(1);
        providerName.setCellValue(PlatformConfig.getValue("sender_name"));
        Cell providerTel = provider.getCell(5);
        providerTel.setCellValue(PlatformConfig.getValue("sender_phone"));
        Row address = sheet.getRow(3);
        Cell addressDetail = address.getCell(1);
        addressDetail.setCellValue(PlatformConfig.getValue("sender_address"));
        return template;
    }

}
