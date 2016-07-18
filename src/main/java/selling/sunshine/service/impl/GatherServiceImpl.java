package selling.sunshine.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import selling.sunshine.dao.AgentDao;
import selling.sunshine.dao.BillDao;
import selling.sunshine.dao.CustomerDao;
import selling.sunshine.dao.CustomerOrderDao;
import selling.sunshine.dao.OrderDao;
import selling.sunshine.dao.OrderItemDao;
import selling.sunshine.model.*;
import selling.sunshine.service.GatherService;
import selling.sunshine.utils.PlatformConfig;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import selling.sunshine.utils.WorkBookUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GatherServiceImpl implements GatherService {
    private Logger logger = LoggerFactory.getLogger(IndentServiceImpl.class);

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerOrderDao customerOrderDao;

    @Autowired
    private AgentDao agentDao;
    
    @Autowired
    private BillDao billDao;

    @Override
    public ResultData generateGather() {
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
        String directory = "/material/journal/gather";
        Workbook template = WorkBookUtil.getGatherTemplate();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        list.forEach(item -> {
            if (item instanceof OrderBill) {
                OrderBill temp = (OrderBill) item;
                String time = format.format(temp.getCreateAt());
                StringBuffer sb = new StringBuffer(parent).append(directory).append(File.separator).append(time);
                File file = new File(sb.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                Map<String, Object> condition = new HashMap<>();
                condition.put("orderId", temp.getOrder().getOrderId());
                ResultData queryResponse = orderDao.queryOrder(condition);
                if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<Order>) queryResponse.getData()).isEmpty()) {
                    Order order = ((List<Order>) queryResponse.getData()).get(0);
                    Workbook workbook = produce(template, order.getAgent(), temp);
                    try {
                        FileOutputStream out = new FileOutputStream(file + File.separator + time + "_" + temp.getBillId() + "_" + order.getAgent().getName() + ".xlsx");
                        workbook.write(out);
                        out.close();
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                        result.setDescription(e.getMessage());
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
                List<Integer> status = new ArrayList<>();
                status.add(1);
                status.add(2);
                status.add(3);
                status.add(4);
                condition.put("status", status);
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
            } else if (item instanceof DepositBill) {
            	DepositBill temp = (DepositBill) item;
                String time = format.format(temp.getCreateAt());
                StringBuffer sb = new StringBuffer(parent).append(directory).append(File.separator).append(time);
                File file = new File(sb.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                Workbook workbook = produce(template, temp);
                try {
                    FileOutputStream out = new FileOutputStream(file + File.separator + time + "_" + temp.getBillId() + "_" + temp.getAgent().getName() + ".xlsx");
                    workbook.write(out);
                    out.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription(e.getMessage());
                }
            }
        });
        return result;
    }

    private Workbook produce(Workbook template, selling.sunshine.model.lite.Agent agent, OrderBill bill) {
        Sheet sheet = template.getSheetAt(0);
        Row time = sheet.getRow(1);
        Cell receiverTime = time.getCell(2);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        receiverTime.setCellValue(format.format(bill.getCreateAt()));
        Cell receiverNo = time.getCell(4);
        receiverNo.setCellValue("NO." + bill.getBillId());
        Row orderNo = sheet.getRow(2);
        Cell orderNoCell = orderNo.getCell(2);
        orderNoCell.setCellValue(bill.getOrder().getOrderId());
        Cell channel = orderNo.getCell(5);
        if(bill.getChannel().equals("wx_pub")){
        	channel.setCellValue("微信付款");
        }else if(bill.getChannel().equals("coffer")){
        	channel.setCellValue("余额付款");
        } else {
        	channel.setCellValue("");
        }
        Row price = sheet.getRow(3);
        Cell priceCell = price.getCell(2);
        priceCell.setCellValue(bill.getBillAmount());
        Row orderTime = sheet.getRow(4);
        Cell orderTimeCell = orderTime.getCell(2);
        orderTimeCell.setCellValue(format.format(bill.getCreateAt()));
        Row customer = sheet.getRow(5);
        Cell name = customer.getCell(2);
        name.setCellValue(agent.getName());
        Cell phone = customer.getCell(5);
        Map<String, Object> condition = new HashMap<>();
        condition.put("agentId", agent.getAgentId());
        ResultData queryResponse = agentDao.queryAgent(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Agent> list = (List<Agent>) queryResponse.getData();
            if (!list.isEmpty()) {
                phone.setCellValue(list.get(0).getPhone());
            }
        }
        Row sellInfo = sheet.getRow(7);
        Cell sellNo = sellInfo.getCell(0);
        sellNo.setCellValue("NO." + bill.getOrder().getOrderId());
        Cell sellPrice1 = sellInfo.getCell(2);
        sellPrice1.setCellValue(bill.getBillAmount());
        Cell sellPrice2 = sellInfo.getCell(3);
        sellPrice2.setCellValue(bill.getBillAmount());
        Cell sellPrice3 = sellInfo.getCell(4);
        sellPrice3.setCellValue(0);
        Cell sellPrice4 = sellInfo.getCell(5);
        sellPrice4.setCellValue(bill.getBillAmount());
        Row sellMan = sheet.getRow(8);
        Cell sellManInfo = sellMan.getCell(1);
        sellManInfo.setCellValue(agent.getName());
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
        Cell channel = orderNo.getCell(5);
        if(bill.getChannel().equals("wx_pub")){
        	channel.setCellValue("微信付款");
        } else {
        	channel.setCellValue("");
        }
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
        Row sellMan = sheet.getRow(8);
        Cell sellManInfo = sellMan.getCell(1);
        sellManInfo.setCellValue("无");
        return template;
    }
    
    private Workbook produce(Workbook template, DepositBill bill) {
    	selling.sunshine.model.lite.Agent agent = bill.getAgent();
    	Sheet sheet = template.getSheetAt(0);
        Row time = sheet.getRow(1);
        Cell receiverTime = time.getCell(2);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        receiverTime.setCellValue(format.format(bill.getCreateAt()));
        Cell receiverNo = time.getCell(4);
        receiverNo.setCellValue("NO." + bill.getBillId());
        Row orderNo = sheet.getRow(2);
        Cell orderNoCell = orderNo.getCell(2);
        orderNoCell.setCellValue(bill.getBillId());
        Cell channel = orderNo.getCell(5);
        if(bill.getChannel().equals("wx_pub")){
        	channel.setCellValue("充值");
        } else {
        	channel.setCellValue("");
        }
        Row price = sheet.getRow(3);
        Cell priceCell = price.getCell(2);
        priceCell.setCellValue(bill.getBillAmount());
        Row orderTime = sheet.getRow(4);
        Cell orderTimeCell = orderTime.getCell(2);
        orderTimeCell.setCellValue(format.format(bill.getCreateAt()));
        Row customer = sheet.getRow(5);
        Cell name = customer.getCell(2);
        name.setCellValue(agent.getName());
        Cell phone = customer.getCell(5);
        Map<String, Object> condition = new HashMap<>();
        condition.put("agentId", agent.getAgentId());
        ResultData queryResponse = agentDao.queryAgent(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Agent> list = (List<Agent>) queryResponse.getData();
            if (!list.isEmpty()) {
                phone.setCellValue(list.get(0).getPhone());
            }
        }
        Row sellInfo = sheet.getRow(7);
        Cell sellNo = sellInfo.getCell(0);
        sellNo.setCellValue("NO." + bill.getBillId());
        Cell sellPrice1 = sellInfo.getCell(2);
        sellPrice1.setCellValue(bill.getBillAmount());
        Cell sellPrice2 = sellInfo.getCell(3);
        sellPrice2.setCellValue(bill.getBillAmount());
        Cell sellPrice3 = sellInfo.getCell(4);
        sellPrice3.setCellValue(0);
        Cell sellPrice4 = sellInfo.getCell(5);
        sellPrice4.setCellValue(bill.getBillAmount());
        Row sellMan = sheet.getRow(8);
        Cell sellManInfo = sellMan.getCell(1);
        sellManInfo.setCellValue("无");
        return template;
    }
}
