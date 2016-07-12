package selling.sunshine.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import selling.sunshine.dao.CustomerDao;
import selling.sunshine.dao.OrderItemDao;
import selling.sunshine.model.Customer;
import selling.sunshine.model.CustomerOrder;
import selling.sunshine.model.OrderBill;
import selling.sunshine.model.OrderItem;
import selling.sunshine.service.DeliverService;
import selling.sunshine.utils.PlatformConfig;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

public class GatherServiceImpl implements DeliverService {
	 private Logger logger = LoggerFactory.getLogger(IndentServiceImpl.class);

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private CustomerDao customerDao;
    
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
	public ResultData produce() {
		return null;
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
		orderTimeCell.setCellValue(item.getCreateAt());
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
	
	private Workbook produce(Workbook template, CustomerOrder item, OrderBill bill) {
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
		orderTimeCell.setCellValue(item.getCreateAt());
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
