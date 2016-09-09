package selling.sunshine.service.impl;

import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.model.selling.order.support.OrderType;
import common.sunshine.utils.IDGenerator;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.CustomerDao;
import selling.sunshine.dao.ExpressDao;
import selling.sunshine.dao.OrderDao;
import common.sunshine.model.selling.express.Express4Agent;
import common.sunshine.model.selling.express.Express4Application;
import common.sunshine.model.selling.express.Express4Customer;
import selling.sunshine.service.IndentService;
import selling.sunshine.utils.PlatformConfig;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.utils.WorkBookUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 7/7/16.
 */
@Service
public class IndentServiceImpl implements IndentService {
    private Logger logger = LoggerFactory.getLogger(IndentServiceImpl.class);

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ExpressDao expressDao;

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
    public <T> ResultData produce(List<T> list) {
        ResultData result = new ResultData();
        String path = IndentServiceImpl.class.getResource("/").getPath();
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/indent";
        Workbook template = WorkBookUtil.getIndentTemplate();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        list.forEach(item -> {
            if (item instanceof OrderItem) {
                OrderItem temp = (OrderItem) item;
                String time = format.format(temp.getCreateAt());
                StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(time);
                File file = new File(sb.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                Workbook workbook = produce(template, temp);
                try {
                    FileOutputStream out = new FileOutputStream(file + "/" + time + "_" + temp.getOrder().getOrderId() + "_" + temp.getCustomer().getName() + ".xlsx");
                    workbook.write(out);
                    out.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription(e.getMessage());
                }
            } else if (item instanceof CustomerOrder) {
                CustomerOrder temp = (CustomerOrder) item;
                String time = format.format(temp.getCreateAt());
                StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(time);
                File file = new File(sb.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                Workbook workbook = produce(template, temp);
                try {
                    FileOutputStream out = new FileOutputStream(file + "/" + time + "_" + temp.getOrderId() + "_" + temp.getReceiverName() + ".xlsx");
                    workbook.write(out);
                    out.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription(e.getMessage());
                }
            } else if (item instanceof EventOrder) {
            	EventOrder temp = (EventOrder) item;
                String time = format.format(temp.getCreateAt());
                StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(time);
                File file = new File(sb.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                Workbook workbook = produce(template, temp);
                try {
                    FileOutputStream out = new FileOutputStream(file + "/" + time + "_" + temp.getOrderId() + "_" + temp.getDoneeName() + ".xlsx");
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
    
    private Workbook produce(Workbook template, EventOrder item) {
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
        totalPrice.setCellValue(item.getGoods().getAgentPrice());
        Cell description = content.getCell(6);
        description.setCellValue("活动");
        Row booker = sheet.getRow(8);
        Cell bookerName = booker.getCell(1);
        bookerName.setCellValue(item.getDoneeName());
        Cell bookerPhone = booker.getCell(5);
        bookerPhone.setCellValue(item.getDoneePhone());
        Row receiveAddr = sheet.getRow(9);
        Cell bookerAddr = receiveAddr.getCell(1);
        bookerAddr.setCellValue(item.getDoneeAddress());
        return template;
    }

    @Override
    public <T> ResultData produceAll(List<T> list) {
        ResultData result = new ResultData();
        HSSFWorkbook wb = new HSSFWorkbook();


        HSSFSheet sheet1 = wb.createSheet();
        wb.setSheetName(0, "代理商订单");
        HSSFRow row = sheet1.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFSheet sheet2 = wb.createSheet();
        wb.setSheetName(1, "客户订单");
        HSSFRow row2 = sheet2.createRow((int) 0);
        
        HSSFSheet sheet3 = wb.createSheet();
        wb.setSheetName(2, "活动订单");
        HSSFRow row3 = sheet3.createRow((int) 0);

        HSSFCell cell = row.createCell((short) 0);
        HSSFCell cell2 = row2.createCell((short) 0);
        HSSFCell cell3 = row3.createCell((short)0);
        cell.setCellValue("订单编号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("代理商");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("顾客");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("联系方式");
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);
        cell.setCellValue("地址");
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);
        cell.setCellValue("商品");
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);
        cell.setCellValue("数量");
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);
        cell.setCellValue("总价");
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);
        cell.setCellValue("购买日期");
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);
        cell.setCellValue("备注");
        cell.setCellStyle(style);
        cell = row.createCell((short) 10);
        cell.setCellValue("是否已发货");
        cell.setCellStyle(style);
        cell = row.createCell((short) 11);
        cell.setCellValue("发货时间");
        cell.setCellStyle(style);
        cell = row.createCell((short) 12);
        cell.setCellValue("快递单号");
        cell.setCellStyle(style);
        cell2.setCellValue("订单编号");
        cell2.setCellStyle(style);
        cell2 = row2.createCell((short) 1);
        cell2.setCellValue("代理商");
        cell2.setCellStyle(style);
        cell2 = row2.createCell((short) 2);
        cell2.setCellValue("顾客");
        cell2.setCellStyle(style);
        cell2 = row2.createCell((short) 3);
        cell2.setCellValue("联系方式");
        cell2.setCellStyle(style);
        cell2 = row2.createCell((short) 4);
        cell2.setCellValue("地址");
        cell2.setCellStyle(style);
        cell2 = row2.createCell((short) 5);
        cell2.setCellValue("商品");
        cell2.setCellStyle(style);
        cell2 = row2.createCell((short) 6);
        cell2.setCellValue("数量");
        cell2.setCellStyle(style);
        cell2 = row2.createCell((short) 7);
        cell2.setCellValue("总价");
        cell2.setCellStyle(style);
        cell2 = row2.createCell((short) 8);
        cell2.setCellValue("购买日期");
        cell2.setCellStyle(style);
        cell2 = row2.createCell((short) 9);
        cell2.setCellValue("是否已发货");
        cell2.setCellStyle(style);
        cell2 = row2.createCell((short) 10);
        cell2.setCellValue("发货时间");
        cell2.setCellStyle(style);
        cell2 = row2.createCell((short) 11);
        cell2.setCellValue("快递单号");
        cell2.setCellStyle(style);
        
        cell3.setCellValue("订单编号");
        cell3.setCellStyle(style);
        cell3 = row3.createCell((short) 1);
        cell3.setCellValue("活动");
        cell3.setCellStyle(style);
        cell3 = row3.createCell((short) 2);
        cell3.setCellValue("顾客");
        cell3.setCellStyle(style);
        cell3 = row3.createCell((short) 3);
        cell3.setCellValue("联系方式");
        cell3.setCellStyle(style);
        cell3 = row3.createCell((short) 4);
        cell3.setCellValue("地址");
        cell3.setCellStyle(style);
        cell3 = row3.createCell((short) 5);
        cell3.setCellValue("商品");
        cell3.setCellStyle(style);
        cell3 = row3.createCell((short) 6);
        cell3.setCellValue("数量");
        cell3.setCellStyle(style);
        cell3 = row3.createCell((short) 7);
        cell3.setCellValue("总价");
        cell3.setCellStyle(style);
        cell3 = row3.createCell((short) 8);
        cell3.setCellValue("订单日期");
        cell3.setCellStyle(style);
        cell3 = row3.createCell((short) 9);
        cell3.setCellValue("是否已发货");
        cell3.setCellStyle(style);
        cell3 = row3.createCell((short) 10);
        cell3.setCellValue("发货时间");
        cell3.setCellStyle(style);
        cell3 = row3.createCell((short) 11);
        cell3.setCellValue("快递单号");
        cell3.setCellStyle(style);
        int k = 1;
        int j = 1;
        int l = 1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderItem) {
                row = sheet1.createRow((int) k);
                OrderItem orderItem = (OrderItem) list.get(i);
                // 第四步，创建单元格，并设置值
                row.createCell((short) 0).setCellValue(orderItem.getOrderItemId());
                Map<String, Object> condition = new HashMap<>();
                condition.put("orderId", orderItem.getOrder().getOrderId());
                Order order = ((List<Order>) orderDao.queryOrder(condition).getData()).get(0);
                row.createCell((short) 1).setCellValue(order.getAgent().getName());
                row.createCell((short) 2).setCellValue(orderItem.getCustomer().getName());
                condition.clear();
                condition.put("customerId", orderItem.getCustomer().getCustomerId());
                Customer customer = ((List<Customer>) customerDao.queryCustomer(condition).getData()).get(0);
                row.createCell((short) 3).setCellValue(customer.getPhone().getPhone());
                row.createCell((short) 4).setCellValue(customer.getAddress().getAddress());
                row.createCell((short) 5).setCellValue(orderItem.getGoods().getName());
                row.createCell((short) 6).setCellValue(orderItem.getGoodsQuantity());
                row.createCell((short) 7).setCellValue(orderItem.getOrderItemPrice());
                cell = row.createCell((short) 8);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(orderItem.getCreateAt()));
                if (order.getType() == OrderType.GIFT) {
                    row.createCell((short) 9).setCellValue("赠品");
                } else {
                    row.createCell((short) 9).setCellValue("");
                }
                if (orderItem.getStatus() == OrderItemStatus.SHIPPED || orderItem.getStatus() == OrderItemStatus.RECEIVED || orderItem.getStatus() == OrderItemStatus.EXCHANGED) {
                    row.createCell((short) 10).setCellValue("是");
                    condition.clear();
                    condition.put("orderItemId", orderItem.getOrderItemId());
                    ResultData queryData = expressDao.queryExpress4Agent(condition);
                    if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK && !((List) queryData.getData()).isEmpty()) {
                        Express4Agent express4Agent = ((List<Express4Agent>) queryData.getData()).get(0);
                        row.createCell((short) 11).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(express4Agent.getCreateAt()));
                        row.createCell((short) 12).setCellValue(express4Agent.getExpressNumber());
                    }
                } else {
                    row.createCell((short) 10).setCellValue("否");
                    row.createCell((short) 11).setCellValue("");
                    row.createCell((short) 12).setCellValue("");
                }
                k++;
            } else if (list.get(i) instanceof CustomerOrder) {
                row = sheet2.createRow((int) j);
                CustomerOrder customerOrder = (CustomerOrder) list.get(i);
                // 第四步，创建单元格，并设置值
                row.createCell((short) 0).setCellValue(customerOrder.getOrderId());
                if (customerOrder.getAgent() != null) {
                    row.createCell((short) 1).setCellValue(customerOrder.getAgent().getName());
                } else {
                    row.createCell((short) 1).setCellValue("");
                }

                row.createCell((short) 2).setCellValue(customerOrder.getReceiverName());
                row.createCell((short) 3).setCellValue(customerOrder.getReceiverPhone());
                row.createCell((short) 4).setCellValue(customerOrder.getReceiverAddress());
                row.createCell((short) 5).setCellValue(customerOrder.getGoods().getName());
                row.createCell((short) 6).setCellValue(customerOrder.getQuantity());
                row.createCell((short) 7).setCellValue(customerOrder.getTotalPrice());
                cell = row.createCell((short) 8);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(customerOrder.getCreateAt()));
                if (customerOrder.getStatus() == OrderItemStatus.SHIPPED || customerOrder.getStatus() == OrderItemStatus.RECEIVED || customerOrder.getStatus() == OrderItemStatus.EXCHANGED) {
                    row.createCell((short) 9).setCellValue("是");
                    Map<String, Object> condition = new HashMap<>();
                    condition.put("orderId", customerOrder.getOrderId());
                    ResultData queryData = expressDao.queryExpress4Customer(condition);
                    if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK && !((List) queryData.getData()).isEmpty()) {
                        Express4Customer express = ((List<Express4Customer>) queryData.getData()).get(0);
                        row.createCell((short) 10).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(express.getCreateAt()));
                        row.createCell((short) 11).setCellValue(express.getExpressNumber());
                    }
                } else {
                    row.createCell((short) 9).setCellValue("否");
                    row.createCell((short) 10).setCellValue("");
                    row.createCell((short) 11).setCellValue("");
                }
                j++;
            } else if (list.get(i) instanceof EventOrder) {
                row = sheet3.createRow((int) l);
                EventOrder eventOrder = (EventOrder) list.get(i);
                // 第四步，创建单元格，并设置值
                row.createCell((short) 0).setCellValue(eventOrder.getOrderId());
                row.createCell((short) 1).setCellValue(eventOrder.getEvent().getTitle());
                row.createCell((short) 2).setCellValue(eventOrder.getDoneeName());
                row.createCell((short) 3).setCellValue(eventOrder.getDoneePhone());
                row.createCell((short) 4).setCellValue(eventOrder.getDoneeAddress());
                row.createCell((short) 5).setCellValue(eventOrder.getGoods().getName());
                row.createCell((short) 6).setCellValue(eventOrder.getQuantity());
                row.createCell((short) 7).setCellValue(eventOrder.getQuantity() * eventOrder.getGoods().getAgentPrice());
                cell = row.createCell((short) 8);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(eventOrder.getCreateAt()));
                if (eventOrder.getOrderStatus() == OrderItemStatus.SHIPPED || eventOrder.getOrderStatus() == OrderItemStatus.RECEIVED || eventOrder.getOrderStatus() == OrderItemStatus.EXCHANGED) {
                    row.createCell((short) 9).setCellValue("是");
                    Map<String, Object> condition = new HashMap<>();
                    condition.put("orderId", eventOrder.getOrderId());
                    ResultData queryData = expressDao.queryExpress4Application(condition);
                    if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK && !((List) queryData.getData()).isEmpty()) {
                        Express4Application express = ((List<Express4Application>) queryData.getData()).get(0);
                        row.createCell((short) 10).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(express.getCreateAt()));
                        row.createCell((short) 11).setCellValue(express.getExpressNumber());
                    }
                } else {
                    row.createCell((short) 9).setCellValue("否");
                    row.createCell((short) 10).setCellValue("");
                    row.createCell((short) 11).setCellValue("");
                }
                l++;
            }

        }

        try {
            String path = IndentServiceImpl.class.getResource("/").getPath();
            int index = path.lastIndexOf("/WEB-INF/classes/");
            String parent = path.substring(0, index);
            String directory = "/material/journal/indent";
            File file = new File(parent + directory);
            if (!file.exists()) {
                file.mkdirs();
            }
            String name = IDGenerator.generate("Indent");
            StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(("发货单汇总_" + name));
            FileOutputStream fout = new FileOutputStream(sb.toString() + ".xls");
            result.setData("发货单汇总_" + name);
            wb.write(fout);
            fout.close();
            wb.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }
        return result;
    }

    @Override
    public ResultData produceSummary(List list) {
        ResultData result = new ResultData();
        String path = IndentServiceImpl.class.getResource("/").getPath();
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/indent";
        File file = new File(parent + directory);
        if (!file.exists()) {
            file.mkdirs();
        }
        Workbook template = WorkBookUtil.getIndentSummaryTemplate();
        Sheet sheet = template.getSheetAt(0);
        int row = 1;
        for (Object item : list) {
            if (item instanceof OrderItem) {
                OrderItem o = (OrderItem) item;
                Customer customer = o.getCustomer();
                Map<String, Object> condition = new HashMap<>();
                condition.put("customerId", ((OrderItem) item).getCustomer().getCustomerId());
                ResultData response = customerDao.queryCustomer(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    customer = ((List<Customer>) response.getData()).get(0);
                }
                Row current = sheet.createRow(row);
                Cell noCell = current.createCell(0);
                noCell.setCellValue(o.getOrderItemId());
                Cell dateCell = current.createCell(1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                dateCell.setCellValue(format.format(o.getCreateAt()));
                Cell consumerNameCell = current.createCell(2);
                consumerNameCell.setCellValue(o.getCustomer().getName());
                Cell phoneCell = current.createCell(3);
                phoneCell.setCellValue(customer.getPhone().getPhone());
                Cell addressCell = current.createCell(4);
                addressCell.setCellValue(customer.getAddress().getAddress());
                condition.clear();
                condition.put("orderId", o.getOrder().getOrderId());
                response = orderDao.queryOrder(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    Cell agentCell = current.createCell(5);
                    Order order = ((List<Order>) response.getData()).get(0);
                    agentCell.setCellValue(order.getAgent().getName());
                }
                Cell productNameCell = current.createCell(6);
                productNameCell.setCellValue(o.getGoods().getName());
                Cell quantityCell = current.createCell(7);
                quantityCell.setCellValue(o.getGoodsQuantity());
                Cell priceCell = current.createCell(8);
                priceCell.setCellValue(o.getOrderItemPrice());
                Cell description = current.createCell(9);
                description.setCellValue((o.getOrder().getType() == OrderType.GIFT) ? "赠品" : "");
                row++;
            } else if (item instanceof CustomerOrder) {
                CustomerOrder c = (CustomerOrder) item;
                Row current = sheet.createRow(row);
                Cell noCell = current.createCell(0);
                noCell.setCellValue(c.getOrderId());
                Cell dateCell = current.createCell(1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                dateCell.setCellValue(format.format(c.getCreateAt()));
                Cell consumerNameCell = current.createCell(2);
                consumerNameCell.setCellValue(c.getReceiverName());
                Cell phoneCell = current.createCell(3);
                phoneCell.setCellValue(c.getReceiverPhone());
                Cell addressCell = current.createCell(4);
                if (c.getAgent() != null) {
                    Cell agentCell = current.createCell(5);
                    agentCell.setCellValue(c.getAgent().getName());
                }
                addressCell.setCellValue(c.getReceiverAddress());
                Cell productNameCell = current.createCell(6);
                productNameCell.setCellValue(c.getGoods().getName());
                Cell quantityCell = current.createCell(7);
                quantityCell.setCellValue(c.getQuantity());
                Cell priceCell = current.createCell(8);
                priceCell.setCellValue(c.getTotalPrice());
                row++;
            }else if (item instanceof EventOrder){
            	EventOrder c = (EventOrder) item;
                Row current = sheet.createRow(row);
                Cell noCell = current.createCell(0);
                noCell.setCellValue(c.getOrderId());
                Cell dateCell = current.createCell(1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                dateCell.setCellValue(format.format(c.getCreateAt()));
                Cell consumerNameCell = current.createCell(2);
                consumerNameCell.setCellValue(c.getDoneeName());
                Cell phoneCell = current.createCell(3);
                phoneCell.setCellValue(c.getDoneePhone());
                Cell addressCell = current.createCell(4);
                addressCell.setCellValue(c.getDoneeAddress());
                Cell productNameCell = current.createCell(6);
                productNameCell.setCellValue(c.getGoods().getName());
                Cell quantityCell = current.createCell(7);
                quantityCell.setCellValue(c.getQuantity());
                Cell priceCell = current.createCell(8);
                priceCell.setCellValue(c.getQuantity() * c.getGoods().getAgentPrice());
                Cell description = current.createCell(9);
                description.setCellValue("活动");
                row++;
            }
        }
        String name = IDGenerator.generate("SUMMARY");
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append("订货统计清单_").append(name).append(".xlsx");
        try {
            FileOutputStream out = new FileOutputStream(sb.toString());
            template.write(out);
            out.close();
            result.setData(new StringBuffer(directory).append("/").append("订货统计清单_").append(name).append(".xlsx").toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }
        return result;
    }
}
