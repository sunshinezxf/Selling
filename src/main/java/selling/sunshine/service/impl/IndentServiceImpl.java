package selling.sunshine.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.CustomerDao;
import selling.sunshine.dao.OrderItemDao;
import selling.sunshine.model.Customer;
import selling.sunshine.model.CustomerOrder;
import selling.sunshine.model.OrderItem;
import selling.sunshine.model.OrderType;
import selling.sunshine.service.IndentService;
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
    private OrderItemDao orderItemDao;

    @Autowired
    private CustomerDao customerDao;

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
        ResultData fetchResponse = orderItemDao.queryOrderItem(condition);
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
        Row content = sheet.getRow(6);
        Cell name = content.getCell(1);
        name.setCellValue(item.getGoods().getName());
        Cell piece = content.getCell(2);
        piece.setCellValue("件");
        Cell price = content.getCell(3);
        price.setCellValue(item.getGoods().getAgentPrice());
        Cell quantity = content.getCell(4);
        quantity.setCellValue(item.getGoodsQuantity());
        Cell totalPrice = content.getCell(5);
        totalPrice.setCellValue(item.getOrderItemPrice());
        if (item.getOrder().getType() == OrderType.GIFT) {
            Cell description = content.getCell(6);
            description.setCellValue("赠送");
        }
        Row booker = sheet.getRow(8);
        Cell bookerName = booker.getCell(1);
        bookerName.setCellValue(item.getCustomer().getName());
        Cell bookerPhone = booker.getCell(5);
        Map<String, Object> condition = new HashMap<>();
        condition.put("customerId", item.getCustomer().getCustomerId());
        ResultData queryResponse = customerDao.queryCustomer(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Customer> list = (List<Customer>) queryResponse.getData();
            if (!list.isEmpty()) {
                bookerPhone.setCellValue(list.get(0).getPhone().getPhone());
            }
        }
        Row receiveAddr = sheet.getRow(9);
        Cell bookerAddr = receiveAddr.getCell(1);
        bookerAddr.setCellValue(item.getReceiveAddress());
        return template;
    }

    private Workbook produce(Workbook template, CustomerOrder item) {
        Sheet sheet = template.getSheetAt(0);
        Row order = sheet.getRow(1);
        Cell orderNo = order.getCell(1);
        orderNo.setCellValue(item.getOrderId());
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
        Row content = sheet.getRow(6);
        Cell name = content.getCell(1);
        name.setCellValue(item.getGoods().getName());
        Cell piece = content.getCell(2);
        piece.setCellValue("件");
        Cell price = content.getCell(3);
        price.setCellValue(item.getGoods().getAgentPrice());
        Cell quantity = content.getCell(4);
        quantity.setCellValue(item.getQuantity());
        Cell totalPrice = content.getCell(5);
        totalPrice.setCellValue((item.getAgent() != null) ? item.getGoods().getAgentPrice() : item.getGoods().getCustomerPrice());
        Row booker = sheet.getRow(8);
        Cell bookerName = booker.getCell(1);
        bookerName.setCellValue(item.getReceiverName());
        Cell bookerPhone = booker.getCell(5);
        bookerPhone.setCellValue(item.getReceiverPhone());
        Row receiveAddr = sheet.getRow(9);
        Cell bookerAddr = receiveAddr.getCell(1);
        bookerAddr.setCellValue(item.getReceiverAddress());
        return template;
    }

}
