package selling.sunshine.service.impl;

import common.sunshine.utils.IDGenerator;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import selling.sunshine.dao.CustomerOrderDao;
import selling.sunshine.dao.EventDao;
import selling.sunshine.dao.ExpressDao;
import selling.sunshine.dao.OrderDao;
import selling.sunshine.dao.OrderItemDao;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderType;
import common.sunshine.model.selling.express.Express;
import selling.sunshine.service.DeliverService;
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

public class DeliverServiceImpl implements DeliverService {
    private Logger logger = LoggerFactory.getLogger(IndentServiceImpl.class);

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private CustomerOrderDao customerOrderDao;

    @Autowired
    private ExpressDao expressDao;

    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private EventDao eventDao;

    @Override
    public ResultData generateDeliver() {
        ResultData result = new ResultData();
        try {
            Workbook workbook = WorkbookFactory.create(new File(PlatformConfig.getValue("deliver_template")));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    //要改 
    @Override
    public <T> ResultData produce(List<T> list) {
        ResultData result = new ResultData();
        String path = IndentServiceImpl.class.getResource("/").getPath();
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/deliver";
        Workbook template = WorkBookUtil.getDeliverTemplate();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        list.forEach(item -> {
            Express express = (Express) item;
            String linkId = express.getLinkId();
            if (!StringUtils.isEmpty(linkId) && linkId.startsWith("ORI")) {
                Map<String, Object> condition = new HashMap<>();
                condition.clear();
                condition.put("orderItemId", linkId);
                ResultData response = orderItemDao.queryOrderItem(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<OrderItem>) response.getData()).isEmpty()) {
                    OrderItem temp = ((List<OrderItem>) response.getData()).get(0);
                    String time = format.format(express.getCreateAt());
                    StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(time);
                    File file = new File(sb.toString());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    Workbook workbook = produce(template, temp, express);
                    try {
                        FileOutputStream out = new FileOutputStream(file + "/" + time + "_" + temp.getOrder().getOrderId() + "_" + temp.getCustomer().getName() + ".xlsx");
                        workbook.write(out);
                        out.close();
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                        result.setDescription(e.getMessage());
                    }
                }

            } else if (!StringUtils.isEmpty(linkId) && linkId.startsWith("CUO")) {
                Map<String, Object> condition = new HashMap<>();
                condition.clear();
                condition.put("orderId", linkId);
                ResultData response = customerOrderDao.queryOrder(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<CustomerOrder>) response.getData()).isEmpty()) {
                    CustomerOrder temp = ((List<CustomerOrder>) response.getData()).get(0);
                    String time = format.format(express.getCreateAt());
                    StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(time);
                    File file = new File(sb.toString());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    Workbook workbook = produce(template, temp, express);
                    try {
                        FileOutputStream out = new FileOutputStream(file + "/" + time + "_" + temp.getOrderId() + "_" + temp.getReceiverName() + ".xlsx");
                        workbook.write(out);
                        out.close();
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                        result.setDescription(e.getMessage());
                    }
                }
            } else if (!StringUtils.isEmpty(linkId) && linkId.startsWith("EOI")) {
                Map<String, Object> condition = new HashMap<>();
                condition.clear();
                condition.put("orderId", linkId);
                ResultData response = eventDao.queryEventOrder(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<EventOrder>) response.getData()).isEmpty()) {
                    EventOrder temp = ((List<EventOrder>) response.getData()).get(0);
                    String time = format.format(express.getCreateAt());
                    StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(time);
                    File file = new File(sb.toString());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    Workbook workbook = produce(template, temp, express);
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
             }
        });
        return result;
    }

    private Workbook produce(Workbook template, OrderItem item, Express express) {
        Sheet sheet = template.getSheetAt(0);
        Row order = sheet.getRow(1);
        Cell orderDate = order.getCell(1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        orderDate.setCellValue(format.format(express.getCreateAt()));
        Cell expressNo = order.getCell(5);
        expressNo.setCellValue(express.getExpressNumber());
        Row consumer = sheet.getRow(2);
        Cell consumerName = consumer.getCell(2);
        consumerName.setCellValue(express.getReceiverName());
        Cell consumerTel = consumer.getCell(5);
        consumerTel.setCellValue(express.getReceiverPhone());
        Row address = sheet.getRow(3);
        Cell addressDetail = address.getCell(2);
        addressDetail.setCellValue(express.getReceiverAddress());
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
        Cell tip = content.getCell(6);
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("orderId", item.getOrder().getOrderId());
        ResultData fetchOrderData = orderDao.queryOrder(condition);
        if (fetchOrderData.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<Order>) fetchOrderData.getData()).isEmpty()) {
            Order orderInfo = ((List<Order>) fetchOrderData.getData()).get(0);
            if (orderInfo.getType() == OrderType.GIFT) {
                tip.setCellValue("赠送");
            } else {
                tip.setCellValue("");
            }
        }
        Row seller = sheet.getRow(8);
        Cell sellerName = seller.getCell(2);
        sellerName.setCellValue(PlatformConfig.getValue("sender_name"));
        Cell sellerPhone = seller.getCell(5);
        sellerPhone.setCellValue(PlatformConfig.getValue("sender_phone"));
        Row sellerAddr = sheet.getRow(9);
        Cell bookerAddr = sellerAddr.getCell(2);
        bookerAddr.setCellValue(PlatformConfig.getValue("sender_address"));
        return template;
    }

    private Workbook produce(Workbook template, CustomerOrder item, Express express) {
        Sheet sheet = template.getSheetAt(0);
        Row order = sheet.getRow(1);
        Cell orderDate = order.getCell(1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        orderDate.setCellValue(format.format(express.getCreateAt()));
        Cell expressNo = order.getCell(5);
        expressNo.setCellValue(express.getExpressNumber());
        Row consumer = sheet.getRow(2);
        Cell consumerName = consumer.getCell(2);
        consumerName.setCellValue(express.getReceiverName());
        Cell consumerTel = consumer.getCell(5);
        consumerTel.setCellValue(express.getReceiverPhone());
        Row address = sheet.getRow(3);
        Cell addressDetail = address.getCell(2);
        addressDetail.setCellValue(express.getReceiverAddress());
        Row content = sheet.getRow(6);
        Cell name = content.getCell(1);
        name.setCellValue(item.getGoods().getName());
        Cell piece = content.getCell(2);
        piece.setCellValue("件");
        Cell price = content.getCell(3);
        if (item.getAgent() != null) {
            price.setCellValue(item.getGoods().getCustomerPrice());
        } else {
            price.setCellValue(item.getGoods().getAgentPrice());
        }
        Cell quantity = content.getCell(4);
        quantity.setCellValue(item.getQuantity());
        Cell totalPrice = content.getCell(5);
        totalPrice.setCellValue(item.getTotalPrice());
        Row seller = sheet.getRow(8);
        Cell sellerName = seller.getCell(2);
        sellerName.setCellValue(PlatformConfig.getValue("sender_name"));
        Cell sellerPhone = seller.getCell(5);
        sellerPhone.setCellValue(PlatformConfig.getValue("sender_phone"));
        Row sellerAddr = sheet.getRow(9);
        Cell bookerAddr = sellerAddr.getCell(2);
        bookerAddr.setCellValue(PlatformConfig.getValue("sender_address"));
        return template;
    }
    
    private Workbook produce(Workbook template, EventOrder item, Express express) {
        Sheet sheet = template.getSheetAt(0);
        Row order = sheet.getRow(1);
        Cell orderDate = order.getCell(1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        orderDate.setCellValue(format.format(express.getCreateAt()));
        Cell expressNo = order.getCell(5);
        expressNo.setCellValue(express.getExpressNumber());
        Row consumer = sheet.getRow(2);
        Cell consumerName = consumer.getCell(2);
        consumerName.setCellValue(express.getReceiverName());
        Cell consumerTel = consumer.getCell(5);
        consumerTel.setCellValue(express.getReceiverPhone());
        Row address = sheet.getRow(3);
        Cell addressDetail = address.getCell(2);
        addressDetail.setCellValue(express.getReceiverAddress());
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
        totalPrice.setCellValue(item.getGoods().getAgentPrice() * item.getQuantity());
        Row seller = sheet.getRow(8);
        Cell sellerName = seller.getCell(2);
        sellerName.setCellValue(PlatformConfig.getValue("sender_name"));
        Cell sellerPhone = seller.getCell(5);
        sellerPhone.setCellValue(PlatformConfig.getValue("sender_phone"));
        Row sellerAddr = sheet.getRow(9);
        Cell bookerAddr = sellerAddr.getCell(2);
        bookerAddr.setCellValue(PlatformConfig.getValue("sender_address"));
        return template;
    }


    @Override
    public ResultData produceSummary(List<Express> list) {
        ResultData result = new ResultData();
        String path = IndentServiceImpl.class.getResource("/").getPath();
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/deliver";
        File file = new File(parent + directory);
        if (!file.exists()) {
            file.mkdirs();
        }
        Workbook template = WorkBookUtil.getDeliverSummaryTemplate();
        Sheet sheet = template.getSheetAt(0);
        int row = 1;
        for (Express item : list) {
            Row current = sheet.createRow(row);
            Cell noCell = current.createCell(0);
            noCell.setCellValue(item.getLinkId());
            Cell expressCell = current.createCell(1);
            expressCell.setCellValue(item.getExpressNumber());
            Cell deliverCell = current.createCell(2);
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            deliverCell.setCellValue(format.format(item.getCreateAt()));
            Cell consumerName = current.createCell(3);
            consumerName.setCellValue(item.getReceiverName());
            Cell phoneCell = current.createCell(4);
            phoneCell.setCellValue(item.getReceiverPhone());
            Cell addressCell = current.createCell(5);
            addressCell.setCellValue(item.getReceiverAddress());
            Cell product = current.createCell(6);
            product.setCellValue(item.getGoodsName());
            row++;
        }
        String name = IDGenerator.generate("DELIVER");
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append("发货统计清单_").append(name).append(".xlsx");
        try {
            FileOutputStream out = new FileOutputStream(sb.toString());
            template.write(out);
            out.close();
            result.setData(new StringBuffer(directory).append("/").append("发货统计清单_").append(name).append(".xlsx").toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }
        return result;
    }
}
