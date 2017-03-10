package selling.sunshine.service.impl;

import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.bill.CustomerOrderBill;
import common.sunshine.model.selling.bill.DepositBill;
import common.sunshine.model.selling.bill.OrderBill;
import common.sunshine.model.selling.bill.RefundBill;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.dao.*;
import selling.sunshine.service.GatherService;
import selling.sunshine.utils.PlatformConfig;
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

    private Workbook produce(Workbook template, common.sunshine.model.selling.agent.lite.Agent agent, OrderBill bill) {
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
        if (bill.getChannel().equals("wx_pub")) {
            channel.setCellValue("微信付款");
        } else if (bill.getChannel().equals("coffer")) {
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
        if (bill.getChannel().equals("wx_pub")) {
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
        common.sunshine.model.selling.agent.lite.Agent agent = bill.getAgent();
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
        if (bill.getChannel().equals("wx_pub")) {
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

    @Override
    public ResultData produceSummary(List list) {
        ResultData result = new ResultData();
        String path = IndentServiceImpl.class.getResource("/").getPath();
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/gather";
        File file = new File(parent + directory);
        if (!file.exists()) {
            file.mkdirs();
        }
        Workbook template = WorkBookUtil.getGatherSummaryTemplate();
        Sheet sheet = template.getSheetAt(0);
        int row = 1;
        for (Object item : list) {
            if (item instanceof OrderBill) {
                OrderBill bill = (OrderBill) item;
                Order order = null;
                Map<String, Object> condition = new HashMap<>();
                condition.put("orderId", bill.getOrder().getOrderId());
                ResultData queryResponse = orderDao.queryOrder(condition);
                if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<Order>) queryResponse.getData()).isEmpty()) {
                    order = ((List<Order>) queryResponse.getData()).get(0);
                } else {
                	continue;
                }
                Row current = sheet.createRow(row);
                Cell noCell = current.createCell(0);
                noCell.setCellValue("NO." + bill.getBillId());
                Cell dateCell = current.createCell(1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                dateCell.setCellValue(format.format(bill.getCreateAt()));
                Cell orderNoCell = current.createCell(2);
                orderNoCell.setCellValue(order.getOrderId());
                Cell channel = current.createCell(3);
                if (bill.getChannel().equals("wx_pub")) {
                    channel.setCellValue("微信付款");
                } else if (bill.getChannel().equals("coffer")) {
                    channel.setCellValue("余额付款");
                } else {
                    channel.setCellValue("");
                }
                Cell priceCell = current.createCell(4);
                priceCell.setCellValue(bill.getBillAmount());
                Cell timeCell = current.createCell(5);
                timeCell.setCellValue(format.format(bill.getCreateAt()));
                Cell customer = current.createCell(6);
                customer.setCellValue(order.getAgent().getName());
                Cell phone = current.createCell(7);
                condition.clear();
                condition.put("agentId", order.getAgent().getAgentId());
                queryResponse = agentDao.queryAgent(condition);
                if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    List<Agent> agentList = (List<Agent>) queryResponse.getData();
                    if (!agentList.isEmpty()) {
                        phone.setCellValue(agentList.get(0).getPhone());
                    }
                }
                Cell sellNo = current.createCell(8);
                sellNo.setCellValue("NO." + order.getOrderId());
                Cell sellPrice1 = current.createCell(9);
                sellPrice1.setCellValue(bill.getBillAmount());
                Cell sellPrice2 = current.createCell(10);
                sellPrice2.setCellValue(bill.getBillAmount());
//                Cell sellPrice3 = current.createCell(11);
//                sellPrice3.setCellValue(0);
                Cell sellPrice4 = current.createCell(11);
                sellPrice4.setCellValue(bill.getBillAmount());
                Cell sellManInfo = current.createCell(12);
                sellManInfo.setCellValue(order.getAgent().getName());
                Cell num = current.createCell(13);
                condition.clear();
                condition.put("orderId", bill.getOrder().getOrderId());
                queryResponse = orderDao.queryOrder(condition);
                if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    Order o = ((List<Order>) queryResponse.getData()).get(0);
                    int accumulate = 0;
                    for (OrderItem i : o.getOrderItems()) {
                        accumulate += i.getGoodsQuantity();
                    }
                    num.setCellValue(accumulate);
                } else {
                    num.setCellValue("");
                }
                row++;
            } else if (item instanceof CustomerOrderBill) {
                CustomerOrderBill bill = (CustomerOrderBill) item;
                CustomerOrder order = null;
                Map<String, Object> condition = new HashMap<>();
                condition.put("orderId", bill.getCustomerOrder().getOrderId());
                List<Integer> status = new ArrayList<>();
                status.add(1);
                status.add(2);
                status.add(3);
                status.add(4);
                condition.put("status", status);
                ResultData queryResponse = customerOrderDao.queryOrder(condition);
                if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<CustomerOrderBill>) queryResponse.getData()).isEmpty()) {
                    order = ((List<CustomerOrder>) queryResponse.getData()).get(0);
                } else {
                	continue;
                }
                Row current = sheet.createRow(row);
                Cell noCell = current.createCell(0);
                noCell.setCellValue("NO." + bill.getBillId());
                Cell dateCell = current.createCell(1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                dateCell.setCellValue(format.format(bill.getCreateAt()));
                Cell orderNoCell = current.createCell(2);
                orderNoCell.setCellValue(order.getOrderId());
                Cell channel = current.createCell(3);
                if (bill.getChannel().equals("wx_pub")) {
                    channel.setCellValue("微信付款");
                } else if (bill.getChannel().equals("coffer")) {
                    channel.setCellValue("余额付款");
                } else {
                    channel.setCellValue("");
                }
                Cell priceCell = current.createCell(4);
                priceCell.setCellValue(bill.getBillAmount());
                Cell timeCell = current.createCell(5);
                timeCell.setCellValue(format.format(bill.getCreateAt()));
                Cell customer = current.createCell(6);
                customer.setCellValue(order.getReceiverName());
                Cell phone = current.createCell(7);
                phone.setCellValue(order.getReceiverPhone());
                Cell sellNo = current.createCell(8);
                sellNo.setCellValue("NO." + order.getOrderId());
                Cell sellPrice1 = current.createCell(9);
                sellPrice1.setCellValue(bill.getBillAmount());
                Cell sellPrice2 = current.createCell(10);
                sellPrice2.setCellValue(bill.getBillAmount());
//                Cell sellPrice3 = current.createCell(11);
//                sellPrice3.setCellValue(0);
                Cell sellPrice4 = current.createCell(11);
                sellPrice4.setCellValue(bill.getBillAmount());
                Cell sellManInfo = current.createCell(12);
                sellManInfo.setCellValue("无");
                Cell num = current.createCell(13);
                num.setCellValue(order.getQuantity());
                row++;
            } else if (item instanceof DepositBill) {
                DepositBill bill = (DepositBill) item;
                Row current = sheet.createRow(row);
                Cell noCell = current.createCell(0);
                noCell.setCellValue("NO." + bill.getBillId());
                Cell dateCell = current.createCell(1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                dateCell.setCellValue(format.format(bill.getCreateAt()));
                Cell orderNoCell = current.createCell(2);
                orderNoCell.setCellValue(bill.getBillId());
                Cell channel = current.createCell(3);
                if (bill.getChannel().equals("wx_pub")) {
                    channel.setCellValue("微信付款");
                } else if (bill.getChannel().equals("coffer")) {
                    channel.setCellValue("余额付款");
                } else {
                    channel.setCellValue("");
                }
                Cell priceCell = current.createCell(4);
                priceCell.setCellValue(bill.getBillAmount());
                Cell timeCell = current.createCell(5);
                timeCell.setCellValue(format.format(bill.getCreateAt()));
                Cell customer = current.createCell(6);
                customer.setCellValue(bill.getAgent().getName());
                Cell phone = current.createCell(7);
                Map<String, Object> condition = new HashMap<>();
                condition.put("agentId", bill.getAgent().getAgentId());
                ResultData queryResponse = agentDao.queryAgent(condition);
                if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    List<Agent> agentList = (List<Agent>) queryResponse.getData();
                    if (!agentList.isEmpty()) {
                        phone.setCellValue(agentList.get(0).getPhone());
                    }
                }
                Cell sellNo = current.createCell(8);
                sellNo.setCellValue("NO." + bill.getBillId());
                Cell sellPrice1 = current.createCell(9);
                sellPrice1.setCellValue(bill.getBillAmount());
                Cell sellPrice2 = current.createCell(10);
                sellPrice2.setCellValue(bill.getBillAmount());
//                Cell sellPrice3 = current.createCell(11);
//                sellPrice3.setCellValue(0);
                Cell sellPrice4 = current.createCell(11);
                sellPrice4.setCellValue(bill.getBillAmount());
                Cell sellManInfo = current.createCell(12);
                sellManInfo.setCellValue("无");
                Cell description = current.createCell(14);
                description.setCellValue("账户充值");
                row++;
            }else if (item instanceof RefundBill){
                RefundBill bill = (RefundBill) item;
//                Map<String,Object> condition=new HashMap<>();
//                condition.put("billId",bill.getBillId());
//                ResultData queryResponse = billDao.queryBillSum(condition);
//                if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
//                    BillSumVo billSumVo= ((List<BillSumVo>)queryResponse.getData()).get(0);
                    Row current = sheet.createRow(row);
                    Cell noCell = current.createCell(0);
                    noCell.setCellValue("NO." + bill.getRefundBillId());
                    Cell dateCell = current.createCell(1);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                    dateCell.setCellValue(format.format(bill.getCreateAt()));
                    Cell orderNoCell = current.createCell(2);
                    orderNoCell.setCellValue(bill.getRefundBillId());
//                    Cell channel = current.createCell(3);
//                    if (billSumVo.getChannel().equals("wx_pub")) {
//                        channel.setCellValue("微信付款");
//                    } else if (billSumVo.getChannel().equals("coffer")) {
//                        channel.setCellValue("余额付款");
//                    } else {
//                        channel.setCellValue("");
//                    }
                    Cell priceCell = current.createCell(4);
                    priceCell.setCellValue("-"+bill.getBillAmount());
                    Cell timeCell = current.createCell(5);
                    timeCell.setCellValue(format.format(bill.getCreateAt()));
//                    Cell account = current.createCell(6);
//                    account.setCellValue("");
//                    Cell phone = current.createCell(7);
//                    Map<String, Object> condition = new HashMap<>();
//                    condition.put("agentId", bill.getAgent().getAgentId());
//                    ResultData queryResponse = agentDao.queryAgent(condition);
//                    if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
//                        List<Agent> agentList = (List<Agent>) queryResponse.getData();
//                        if (!agentList.isEmpty()) {
//                            phone.setCellValue(agentList.get(0).getPhone());
//                        }
//                    }
                    Cell sellNo = current.createCell(8);
                    sellNo.setCellValue("NO." + bill.getBillId());
                    Cell sellPrice1 = current.createCell(9);
                    sellPrice1.setCellValue("-"+bill.getBillAmount());
                    Cell sellPrice2 = current.createCell(10);
                    sellPrice2.setCellValue("-"+bill.getBillAmount());
//                Cell sellPrice3 = current.createCell(11);
//                sellPrice3.setCellValue(0);
                    Cell sellPrice4 = current.createCell(11);
                    sellPrice4.setCellValue(bill.getBillAmount());
//                    Cell sellManInfo = current.createCell(12);
//                    sellManInfo.setCellValue("无");
                    Cell description = current.createCell(14);
                    description.setCellValue("退款单");
                    row++;
//                }else {
//                    continue;
//                }
            }
        }
        String name = IDGenerator.generate("SUMMARY");
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append("收款统计清单_").append(name).append(".xlsx");
        try {
            FileOutputStream out = new FileOutputStream(sb.toString());
            template.write(out);
            out.close();
            result.setData(new StringBuffer(directory).append("/").append("收款统计清单_").append(name).append(".xlsx").toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }
        return result;
    }
}
