package selling.sunshine.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.dao.BillDao;
import selling.sunshine.dao.CustomerDao;
import selling.sunshine.dao.CustomerOrderDao;
import selling.sunshine.dao.OrderItemDao;
import selling.sunshine.model.*;
import selling.sunshine.service.DeliverService;
import selling.sunshine.utils.PlatformConfig;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import selling.sunshine.utils.WorkBookUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GatherServiceImpl implements DeliverService {
    private Logger logger = LoggerFactory.getLogger(IndentServiceImpl.class);

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private CustomerDao customerDao;
    
    @Autowired
    private CustomerOrderDao customerOrderDao;

    @Autowired
    private BillDao billDao;
    
    @Override
    public ResultData generateDeliver() {
        ResultData result = new ResultData();
        try {
            Workbook workbook = WorkbookFactory.create(new File(PlatformConfig.getValue("gather_template")));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public <T> ResultData produce(List<T> list) {
        ResultData result = new ResultData();
        String path = IndentServiceImpl.class.getResource("/").getPath();
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/indent";
        Workbook template = WorkBookUtil.getIndentTemplate();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        list.forEach(item -> {
            if (item instanceof OrderBill) {
            	OrderBill temp = (OrderBill)item;
                String time = format.format(temp.getCreateAt());
                StringBuffer sb = new StringBuffer(parent).append(directory).append(File.separator).append(time);
                File file = new File(sb.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                Map<String, Object> condition = new HashMap<>();
                condition.put("orderId", temp.getOrder().getOrderId());
                condition.put("status", 1);
                condition.put("status", 2);
                condition.put("status", 3);
                condition.put("status", 4);
                ResultData queryResponse = orderItemDao.queryOrderItem(condition);
                if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<OrderItem>) queryResponse.getData()).isEmpty()) {
                	List<OrderItem> orderItems = (List<OrderItem>) queryResponse.getData();
                	for(OrderItem orderItem : orderItems){
	                    Workbook workbook = produce(template, orderItem, temp);
	                    try {
	                        FileOutputStream out = new FileOutputStream(file + File.separator + time + "_" + orderItem.getOrderItemId() + "_" + orderItem.getCustomer().getName() + ".xlsx");
	                        workbook.write(out);
	                        out.close();
	                    } catch (Exception e) {
	                        logger.error(e.getMessage());
	                        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
	                        result.setDescription(e.getMessage());
	                    }
                	}
                }
            } else if (item instanceof CustomerOrderBill) {
                CustomerOrderBill temp = (CustomerOrderBill) item;
                String time = format.format(temp.getCreateAt());
                StringBuffer sb = new StringBuffer(parent).append(directory).append(File.separator).append(time);
                File file = new File(sb.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                Map<String, Object> condition = new HashMap<>();
                condition.put("orderId", temp.getCustomerOrder().getOrderId());
                condition.put("status", 1);
                condition.put("status", 2);
                condition.put("status", 3);
                condition.put("status", 4);
                ResultData queryResponse = customerOrderDao.queryOrder(condition);
                if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<CustomerOrderBill>) queryResponse.getData()).isEmpty()) {
                    CustomerOrder customerOrder = ((List<CustomerOrder>) queryResponse.getData()).get(0);
                    Workbook workbook = produce(template, customerOrder, temp);
                    try {
                        FileOutputStream out = new FileOutputStream(file + File.separator + time + "_" + customerOrder.getOrderId() + "_" + customerOrder.getReceiverName() + ".xlsx");
                        workbook.write(out);
                        out.close();
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                        result.setDescription(e.getMessage());
                    }
                }
            }
        });
        return result;
    }

    private Workbook produce(Workbook template, OrderItem item, OrderBill bill) {
        Sheet sheet = template.getSheetAt(0);
        Row time = sheet.getRow(1);
        Cell receiverTime = time.getCell(2);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        receiverTime.setCellValue(format.format(bill.getCreateAt()));
        Cell receiverNo = time.getCell(4);
        receiverNo.setCellValue("NO." + bill.getBillId());
        Row orderNo = sheet.getRow(2);
        Cell orderNoCell = orderNo.getCell(2);
        orderNoCell.setCellValue(item.getOrderItemId());
        Row price = sheet.getRow(3);
        Cell priceCell = price.getCell(2);
        priceCell.setCellValue(item.getOrderItemPrice());
        Row orderTime = sheet.getRow(4);
        Cell orderTimeCell = orderTime.getCell(2);
        orderTimeCell.setCellValue(format.format(item.getCreateAt()));
        Row customer = sheet.getRow(5);
        Cell name = customer.getCell(2);
        name.setCellValue(item.getCustomer().getName());
        Cell phone = customer.getCell(5);
        Map<String, Object> condition = new HashMap<>();
        condition.put("customerId", item.getCustomer().getCustomerId());
        ResultData queryResponse = customerDao.queryCustomer(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Customer> list = (List<Customer>) queryResponse.getData();
            if (!list.isEmpty()) {
                phone.setCellValue(list.get(0).getPhone().getPhone());
            }
        }
        Row sellInfo = sheet.getRow(7);
        Cell sellNo = sellInfo.getCell(0);
        sellNo.setCellValue("NO." + item.getOrderItemId());
        Cell sellPrice1 = sellInfo.getCell(2);
        sellPrice1.setCellValue(item.getOrderItemPrice());
        Cell sellPrice2 = sellInfo.getCell(3);
        sellPrice2.setCellValue(item.getOrderItemPrice());
        Cell sellPrice3 = sellInfo.getCell(4);
        sellPrice3.setCellValue(0);
        Cell sellPrice4 = sellInfo.getCell(5);
        sellPrice4.setCellValue(item.getOrderItemPrice());
        return template;
    }

    private Workbook produce(Workbook template, CustomerOrder item, CustomerOrderBill bill) {
        Sheet sheet = template.getSheetAt(0);
        Row time = sheet.getRow(1);
        Cell receiverTime = time.getCell(2);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        receiverTime.setCellValue(format.format(bill.getCreateAt()));
        Cell receiverNo = time.getCell(4);
        receiverNo.setCellValue("NO." + bill.getBillId());
        Row orderNo = sheet.getRow(2);
        Cell orderNoCell = orderNo.getCell(2);
        orderNoCell.setCellValue(item.getOrderId());
        Row price = sheet.getRow(3);
        Cell priceCell = price.getCell(2);
        priceCell.setCellValue(item.getTotalPrice());
        Row orderTime = sheet.getRow(4);
        Cell orderTimeCell = orderTime.getCell(2);
        orderTimeCell.setCellValue(format.format(item.getCreateAt()));
        Row customer = sheet.getRow(5);
        Cell name = customer.getCell(2);
        name.setCellValue(item.getReceiverName());
        Cell phone = customer.getCell(5);
        phone.setCellValue(item.getReceiverPhone());
        Row sellInfo = sheet.getRow(7);
        Cell sellNo = sellInfo.getCell(0);
        sellNo.setCellValue("NO." + item.getOrderId());
        Cell sellPrice1 = sellInfo.getCell(2);
        sellPrice1.setCellValue(item.getTotalPrice());
        Cell sellPrice2 = sellInfo.getCell(3);
        sellPrice2.setCellValue(item.getTotalPrice());
        Cell sellPrice3 = sellInfo.getCell(4);
        sellPrice3.setCellValue(0);
        Cell sellPrice4 = sellInfo.getCell(5);
        sellPrice4.setCellValue(item.getTotalPrice());
        return template;
    }
}
