package selling.sunshine.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Charge;
import common.sunshine.model.selling.admin.Admin;
import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.bill.CustomerOrderBill;
import common.sunshine.model.selling.bill.OrderBill;
import common.sunshine.model.selling.bill.RefundBill;
import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.event.PromotionEvent;
import common.sunshine.model.selling.event.support.PromotionConfig;
import common.sunshine.model.selling.express.Express;
import common.sunshine.model.selling.express.Express4Agent;
import common.sunshine.model.selling.express.Express4Customer;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.model.selling.order.support.OrderStatus;
import common.sunshine.model.selling.user.User;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.pagination.MobilePage;
import common.sunshine.pagination.MobilePageParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import common.sunshine.utils.SortRule;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import selling.sunshine.form.ExpressForm;
import selling.sunshine.form.OrderItemForm;
import selling.sunshine.form.PayForm;
import selling.sunshine.form.TimeRangeForm;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.service.*;
import selling.sunshine.utils.*;
import selling.sunshine.vo.customer.CustomerVo;
import selling.sunshine.vo.order.OrderItemSum;
import selling.sunshine.vo.order.OrderItemVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sunshine on 4/11/16.
 */
@RequestMapping("/order")
@RestController
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private BillService billService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private IndentService indentService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private LogService logService;

    @RequestMapping(method = RequestMethod.GET, value = "/check")
    public ModelAndView handle() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/check");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/check")
    public MobilePage<Order> handle(MobilePageParam param) {
        MobilePage<Order> result = new MobilePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(2);
        condition.put("status", status);
        List<SortRule> rule = new ArrayList<>();
        rule.add(new SortRule("create_time", "asc"));
        condition.put("sort", rule);
        ResultData fetchResponse = orderService.fetchOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (MobilePage<Order>) fetchResponse.getData();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/customerOrder/check")
    public MobilePage<CustomerOrder> customerOrderHandle(MobilePageParam param) {
        MobilePage<CustomerOrder> result = new MobilePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(1);
        condition.put("status", status);
        List<SortRule> rule = new ArrayList<>();
        rule.add(new SortRule("create_time", "asc"));
        condition.put("sort", rule);
        ResultData fetchResponse = orderService.fetchCustomerOrder(condition,
                param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (MobilePage<CustomerOrder>) fetchResponse.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/overview");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/submitedTableOrderOverview")
    public DataTablePage<Order> submitedTableOrderOverview(DataTableParam param) {
        DataTablePage<Order> result = new DataTablePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(0);
        status.add(1);
        condition.put("status", status);
        ResultData fetchResponse = orderService.fetchOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Order>) fetchResponse.getData();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/checkOrder")
    public DataTablePage<Order> checkOrder(DataTableParam param) {
        DataTablePage<Order> result = new DataTablePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(2);
        status.add(3);
        condition.put("status", status);
        ResultData fetchResponse = orderService.fetchOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Order>) fetchResponse.getData();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public DataTablePage<OrderItemSum> overview(DataTableParam param) {
        DataTablePage<OrderItemSum> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (!StringUtils.isEmpty(user.getAgent())) {
            condition.put("agentId", user.getAgent().getAgentId());
        }
        ResultData response = orderService.fetchOrderItemSum(condition, param);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<OrderItemSum>) response.getData();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/payedOrderOverview")
    public DataTablePage<Order> payedOrderOverview(DataTableParam param) {
        DataTablePage<Order> result = new DataTablePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(2);
        status.add(3);
        status.add(4);
        condition.put("status", status);
        ResultData fetchResponse = orderService.fetchOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Order>) fetchResponse.getData();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/finishedOrderOverview")
    public DataTablePage<Order> finishedOrderOverview(DataTableParam param) {
        DataTablePage<Order> result = new DataTablePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(5);
        condition.put("status", status);
        ResultData fetchResponse = orderService.fetchOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Order>) fetchResponse.getData();
        }
        return result;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/customerOrder/overview")
    public ModelAndView customerOrderOverview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/customerOrder");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/customerOrder/overview/{statusCode}")
    public DataTablePage<CustomerOrder> customerOrderOverview(DataTableParam param, @PathVariable("statusCode") int statusCode) {
        DataTablePage<CustomerOrder> result = new DataTablePage<>();
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        switch (statusCode) {
            case 0:
                status.add(0);
                break;
            case 1:
                status.add(1);
                break;
            case 2:
                status.add(2);
                break;
            case 3:
                status.add(3);
                break;
        }
        condition.put("status", status);
        ResultData fetchResponse = orderService.fetchCustomerOrder(condition,
                param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<CustomerOrder>) fetchResponse.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/viewexpress/{type}/{orderId}")
    public ModelAndView viewExpress(@PathVariable("type") String type, @PathVariable("orderId") String orderId) {
        ModelAndView view = new ModelAndView();
        view.setViewName("/agent/order/express");
        Map<String, Object> condition = new HashMap<String, Object>();
        ResultData fetchExpressResponse = null;
        if (orderId.startsWith("ORI")) {
            condition.put("orderItemId", orderId);
            fetchExpressResponse = expressService.fetchExpress4Agent(condition);
        } else if (orderId.startsWith("CUO")) {
            condition.put("orderId", orderId);
            fetchExpressResponse = expressService.fetchExpress4Customer(condition);
        } else {
            condition.put("orderId", orderId);
            fetchExpressResponse = expressService.fetchExpress4Application(condition);
        }
        if (fetchExpressResponse == null) {
            view.addObject("type", "2");//订单号错误
            return view;
        }
        if (fetchExpressResponse.getResponseCode() != ResponseCode.RESPONSE_OK || ((List<Express>) fetchExpressResponse.getData()).isEmpty()) {
            view.addObject("type", "1");//没有快递信息
            return view;
        }
        Express express = ((List<Express>) fetchExpressResponse.getData()).get(0);
        String expressNumber = express.getExpressNumber();
        if (expressNumber == null || expressNumber.equals("")) {
            view.addObject("type", "1");//没有快递信息
            return view;
        }
        view.addObject("type", type);
        view.addObject("expressNumber", expressNumber);
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/express")
    public ModelAndView express() {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        List<Express> expresses = new ArrayList<>();
        // 查询所有状态为已付款的代理商订单
        List<Integer> status = new ArrayList<>();
        status.add(2);
        status.add(3);
        condition.put("status", status);
        ResultData fetchResponse = orderService.fetchOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Order> list = (List<Order>) fetchResponse.getData();
            for (Order order : list) {
                for (OrderItem item : order.getOrderItems()) {
                    if (item.getStatus() == OrderItemStatus.PAYED) {
                        Customer customer = item.getCustomer();
                        Goods4Agent goods = item.getGoods();
                        Express4Agent express = new Express4Agent("尚未设置",
                                PlatformConfig.getValue("sender_name"),
                                PlatformConfig.getValue("sender_phone"),
                                PlatformConfig.getValue("sender_address"),
                                customer.getName(), customer.getPhone().getPhone(),
                                customer.getAddress().getAddress(), goods.getName());
                        express.setLinkId(item.getOrderItemId());
                        express.setGoodsQuantity(item.getGoodsQuantity());
                        expresses.add(express);
                    }

                }
            }
        }
        // 查询所有顾客订单
        condition.clear();
        status.clear();
        status.add(1);
        condition.put("status", status);
        fetchResponse = orderService.fetchCustomerOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CustomerOrder> list = (List<CustomerOrder>) fetchResponse
                    .getData();
            for (CustomerOrder item : list) {
                Goods4Customer goods = item.getGoods();
                Express4Customer express = new Express4Customer("尚未设置",
                        PlatformConfig.getValue("sender_name"),
                        PlatformConfig.getValue("sender_phone"),
                        PlatformConfig.getValue("sender_address"),
                        item.getReceiverName(), item.getReceiverPhone(),
                        item.getReceiverAddress(), goods.getName());
                express.setLinkId(item.getOrderId());
                express.setGoodsQuantity(item.getQuantity());
                expresses.add(express);
            }
        }
        Collections.sort(expresses);
        for (int i = 0; i < expresses.size(); i++) {
            expresses.get(i).setExpressId("expressNumber" + i);
        }
        view.addObject("expressList", expresses);
        view.setViewName("/backend/order/express");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/express")
    public ModelAndView express(String orderId, String customerOrderId) {
        ModelAndView view = new ModelAndView();
        if (StringUtils.isEmpty(orderId)
                && StringUtils.isEmpty(customerOrderId)) {
            view.setViewName("/backend/order/express");
            return view;
        }
        List<Express> expressList = new ArrayList<>();
        if (!StringUtils.isEmpty(orderId)) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("orderId", orderId);
            ResultData orderData = orderService.fetchOrder(condition);
            Order order = ((List<Order>) orderData.getData()).get(0);
            // 验证order的每一项orderItem购买的商品的数量与相应的价格是否一致
            // 若不一致，则将不一致的那一项删除，并且把钱退回给代理商并告知他
            // 同时，根据不同情况修改order的状态和orderItem的状态
            for (OrderItem item : order.getOrderItems()) {
                if (item.getStatus() == OrderItemStatus.PAYED) {
                    Customer customer = item.getCustomer();
                    Goods4Agent goods = item.getGoods();
                    Express4Agent express = new Express4Agent("尚未设置",
                            PlatformConfig.getValue("sender_name"),
                            PlatformConfig.getValue("sender_phone"),
                            PlatformConfig.getValue("sender_address"),
                            customer.getName(), customer.getPhone().getPhone(),
                            customer.getAddress().getAddress(), goods.getName());
                    express.setLinkId(item.getOrderItemId());
                    express.setGoodsQuantity(item.getGoodsQuantity());
                    expressList.add(express);
                }
            }
        } else {
            Map<String, Object> condition = new HashMap<>();
            condition.put("orderId", customerOrderId);
            ResultData fetchResponse = orderService
                    .fetchCustomerOrder(condition);
            if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                CustomerOrder order = ((List<CustomerOrder>) orderService.fetchCustomerOrder(condition).getData()).get(0);
                Goods4Customer goods = order.getGoods();
                Express4Customer express = new Express4Customer("尚未设置",
                        PlatformConfig.getValue("sender_name"),
                        PlatformConfig.getValue("sender_phone"),
                        PlatformConfig.getValue("sender_address"),
                        order.getReceiverName(), order.getReceiverPhone(),
                        order.getReceiverAddress(), goods.getName());
                express.setLinkId(order.getOrderId());
                express.setGoodsQuantity(order.getQuantity());
                expressList.add(express);
            }
        }
        Collections.sort(expressList);
        for (int i = 0; i < expressList.size(); i++) {
            expressList.get(i).setExpressId("expressNumber" + i);
        }
        view.addObject("expressList", expressList);
        view.setViewName("/backend/order/express");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/expressData")
    @ResponseBody
    public String express(@RequestBody ExpressForm form, HttpSession session) {
        List<Express> itemForms = form.getExpressItem();
        List<Express> expresseList = new ArrayList<>();
        for (Express item : itemForms) {
            String linkId = item.getLinkId();
            if (!StringUtils.isEmpty(linkId) && linkId.startsWith("ORI")) {
                Express4Agent express = new Express4Agent("",
                        item.getSenderName(), item.getSenderPhone(),
                        item.getSenderAddress(), item.getReceiverName(),
                        item.getReceiverPhone(), item.getReceiverAddress(),
                        item.getGoodsName());
                express.setGoodsQuantity(item.getGoodsQuantity());
                OrderItem temp = new OrderItem();
                Map<String, Object> condition = new HashMap<>();
                condition.put("orderItemId", linkId);
                ResultData fetchResponse = orderService.fetchOrderItem(condition);
                if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    temp = ((List<OrderItem>) fetchResponse.getData()).get(0);
                }
                express.setLinkId(linkId);
                express.setItem(temp);
                expresseList.add(express);
            } else if (linkId.startsWith("CUO")) {
                Express4Customer express = new Express4Customer(
                        "", item.getSenderName(),
                        item.getSenderPhone(), item.getSenderAddress(),
                        item.getReceiverName(), item.getReceiverPhone(),
                        item.getReceiverAddress(), item.getGoodsName());
                express.setGoodsQuantity(item.getGoodsQuantity());
                CustomerOrder temp = new CustomerOrder();
                Map<String, Object> condition = new HashMap<>();
                condition.put("orderId", linkId);
                ResultData fetchResponse = orderService.fetchCustomerOrder(condition);
                if (fetchResponse.getResponseCode() ==
                        ResponseCode.RESPONSE_OK) {
                    temp = ((List<CustomerOrder>) fetchResponse.getData()).get(0);
                }
                express.setLinkId(linkId);
                express.setOrder(temp);
                expresseList.add(express);
            }
        }
        session.setAttribute("expresseList", expresseList);
        return "";

    }


    @RequestMapping(method = RequestMethod.GET, value = "/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        List<Express> expresseList = (List<Express>) session.getAttribute("expresseList");
        if (expresseList == null) {
            return null;
        }

        String context = request.getSession().getServletContext().getRealPath("/");
        StringBuffer path = new StringBuffer(context);
        path.append(PlatformConfig.getValue("express_template"));
        logger.debug(path.toString());
        File file = new File(path.toString());
        if (!file.exists()) {
            logger.error("文件不存在");
            return "";
        }
        response.reset();
        response.setContentType("application/x-download;charset=utf-8");
        //response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode("快递单.xls", "utf-8"));
        OutputStream os = response.getOutputStream();

        NPOIFSFileSystem pkg = new NPOIFSFileSystem(file);
        Workbook workbook = new HSSFWorkbook(pkg.getRoot(), true);
        for (int row = 3, i = 0; i < expresseList.size(); i++, row++) {
            Express express = expresseList.get(i);
            Sheet sheet = workbook.getSheetAt(0);
            Row current = sheet.createRow(row);
            Cell senderName = current.createCell(2);
            senderName.setCellValue(PlatformConfig.getValue("sender_name"));
            Cell senderPhone = current.createCell(3);
            senderPhone.setCellValue(PlatformConfig.getValue("sender_phone"));
            Cell senderAddress = current.createCell(7);
            senderAddress.setCellValue(PlatformConfig.getValue("sender_address"));
            Cell receiverName = current.createCell(8);
            receiverName.setCellValue(expresseList.get(i).getReceiverName());
            Cell receiverPhone = current.createCell(9);
            receiverPhone.setCellValue(express.getReceiverPhone());
            Cell receiverAddress = current.createCell(13);
            receiverAddress.setCellValue(express.getReceiverAddress());
            Cell goods = current.createCell(14);
            goods.setCellValue(express.getGoodsName());
            Cell description = current.createCell(22);
            StringBuffer descriptionContent = new StringBuffer();
            descriptionContent.append(express.getGoodsQuantity()).append("盒");
            if (!StringUtils.isEmpty(express.getDescription())) {
                descriptionContent.append(", ").append(express.getDescription());
            }
            description.setCellValue(descriptionContent.toString());
            Cell orderNo = current.createCell(37);
            if (expresseList.get(i).getLinkId().startsWith("ORI")) {
                Express4Agent ea = (Express4Agent) express;
                orderNo.setCellValue(ea.getItem().getOrderItemId());
            } else {
                Express4Customer ec = (Express4Customer) express;
                orderNo.setCellValue(ec.getOrder().getOrderId());
            }

        }
        // 写入数据并关闭文件
        workbook.write(os);
        workbook.close();
        os.flush();
        os.close();
        session.removeAttribute("expresseList");
        return null;

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/download/{status}")
    public String download(HttpServletResponse response, @PathVariable("status") String status) throws IOException {
        if (StringUtils.isEmpty(status)) {
            return null;
        }
        Workbook workbook = WorkBookUtil.getExpressTemplate();
        if (StringUtils.isEmpty(workbook)) {
            logger.error("快递单模板文件不存在");
            return "";
        }
        List<Express> expresses = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        //查询当前是否有正在进行的满赠活动
        Map<String, PromotionConfig> config = new HashMap<>();
        PromotionEvent event = null;
        ResultData fetchResponse = eventService.fetchPromotionEvent(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            event = ((List<PromotionEvent>) fetchResponse.getData()).get(0);
            for (PromotionConfig item : event.getConfig()) {
                config.put(item.getBuyGoods().getGoodsId(), item);
            }
        }
        //查询代理商的该状态的所有有效订单信息
        condition.clear();
        switch (status) {
            case "PAYED":
                List<Integer> statusList = new ArrayList<>(Arrays.asList(OrderStatus.PAYED.getCode(), OrderStatus.PATIAL_SHIPMENT.getCode()));
                condition.put("status", statusList);
                break;
        }
        fetchResponse = orderService.fetchOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Order> list = (List<Order>) fetchResponse.getData();
            for (Order order : list) {
                for (OrderItem item : order.getOrderItems()) {
                    if (item.getStatus() == OrderItemStatus.PAYED) {
                        Customer customer = item.getCustomer();
                        Goods4Agent goods = item.getGoods();
                        Express4Agent express = new Express4Agent(PlatformConfig.getValue("sender_name"), PlatformConfig.getValue("sender_phone"), PlatformConfig.getValue("sender_address"), customer.getName(), customer.getPhone().getPhone(), customer.getAddress().getAddress(), goods.getName());
                        express.setLinkId(item.getOrderItemId());
                        express.setGoodsQuantity(item.getGoodsQuantity());
                        express.setItem(item);
                        PromotionConfig currentConfig = config.get(item.getGoods().getGoodsId());
                        if ("PAYED".equals(status) && !StringUtils.isEmpty(event) && !StringUtils.isEmpty(currentConfig) && item.getGoodsQuantity() >= currentConfig.getCriterion()) {
                            String name = StringUtils.isEmpty(currentConfig.getGiveGoods().getNickname()) ? currentConfig.getGiveGoods().getName() : currentConfig.getGiveGoods().getNickname();
                            express.setDescription("赠送" + item.getGoodsQuantity() * currentConfig.getGive() / currentConfig.getFull() + "盒" + name);
                        }
                        expresses.add(express);
                    }
                }
            }
        } else if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            logger.debug("当前暂无代理商该状态下的订单信息");
        } else {
            logger.debug("获取代理商该状态的订单数据异常");
        }
        //查询顾客订单中该状态的所有有效订单信息
        condition.clear();
        switch (status) {
            case "PAYED":
                List<Integer> statusList = new ArrayList<>(Arrays.asList(OrderItemStatus.PAYED.getCode()));
                condition.put("status", statusList);
                condition.put("blockFlag", false);
                break;
        }
        fetchResponse = orderService.fetchCustomerOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CustomerOrder> list = (List<CustomerOrder>) fetchResponse.getData();
            for (CustomerOrder item : list) {
                Goods4Customer goods = item.getGoods();
                Express4Customer express = new Express4Customer(PlatformConfig.getValue("sender_name"), PlatformConfig.getValue("sender_phone"), PlatformConfig.getValue("sender_address"), item.getReceiverName(), item.getReceiverPhone(), item.getReceiverAddress(), goods.getName());
                express.setLinkId(item.getOrderId());
                express.setOrder(item);
                express.setGoodsQuantity(item.getQuantity());
                PromotionConfig currentConfig = config.get(item.getGoods().getGoodsId());
                if ("PAYED".equals(status) && !StringUtils.isEmpty(event) && !StringUtils.isEmpty(currentConfig) && item.getQuantity() >= currentConfig.getCriterion()) {
                    String name = StringUtils.isEmpty(currentConfig.getGiveGoods().getNickname()) ? currentConfig.getGiveGoods().getName() : currentConfig.getGiveGoods().getNickname();
                    express.setDescription("赠送" + item.getQuantity() * currentConfig.getGive() / currentConfig.getFull() + "盒" + name);
                }
                expresses.add(express);
            }
        } else if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            logger.debug("当前暂无顾客该状态下的订单信息");
        } else {
            logger.debug("获取顾客该状态的订单数据异常");
        }
        response.setContentType("application/x-download;charset=utf-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("快递单.xls", "utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        OutputStream os = response.getOutputStream();
        for (int row = 3, i = 0; i < expresses.size(); i++, row++) {
            Express express = expresses.get(i);
            Sheet sheet = workbook.getSheetAt(0);
            Row current = sheet.createRow(row);
            Cell senderName = current.createCell(2);
            senderName.setCellValue(PlatformConfig.getValue("sender_name"));
            Cell senderPhone = current.createCell(3);
            senderPhone.setCellValue(PlatformConfig.getValue("sender_phone"));
            Cell senderAddress = current.createCell(7);
            senderAddress.setCellValue(PlatformConfig.getValue("sender_address"));
            Cell receiverName = current.createCell(8);
            receiverName.setCellValue(expresses.get(i).getReceiverName());
            Cell receiverPhone = current.createCell(9);
            receiverPhone.setCellValue(express.getReceiverPhone());
            Cell receiverAddress = current.createCell(13);
            receiverAddress.setCellValue(express.getReceiverAddress());
            Cell goods = current.createCell(14);
            goods.setCellValue(express.getGoodsName());
            Cell description = current.createCell(22);
            StringBuffer descriptionContent = new StringBuffer();
            descriptionContent.append(express.getGoodsQuantity()).append("盒").append(express.getGoodsName());
            if (!StringUtils.isEmpty(express.getDescription())) {
                descriptionContent.append(", ").append(express.getDescription());
            }
            description.setCellValue(descriptionContent.toString());
            Cell orderNo = current.createCell(37);
            if (expresses.get(i).getLinkId().startsWith("ORI")) {
                Express4Agent ea = (Express4Agent) express;
                orderNo.setCellValue(ea.getItem().getOrderItemId());
            } else {
                Express4Customer ec = (Express4Customer) express;
                orderNo.setCellValue(ec.getOrder().getOrderId());
            }

        }
        workbook.write(os);
        os.flush();
        os.close();
        return "";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/downloadOrderExcel")
    public String downloadOrderExcel(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException, WriteException {
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(2);
        condition.put("status", status);
        List<Order> orderList = (List<Order>) orderService.fetchOrder(condition).getData();

        condition.clear();
        status.clear();
        status.add(1);
        condition.put("status", status);
        List<CustomerOrder> customerOrderList = (List<CustomerOrder>) orderService.fetchCustomerOrder(condition).getData();
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode("已付款订单.xls", "utf-8"));
        OutputStream os = response.getOutputStream();
        WritableWorkbook book = null;
        WritableSheet sheet1;
        WritableSheet sheet2;
        //book = Workbook.createWorkbook(os);
        book = jxl.Workbook.createWorkbook(os);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (orderList != null) {
            sheet1 = book.createSheet(" 代理商订单 ", 0);
            String headerArr[] = {"订单编号", "订单项编号", "代理商", "顾客", "商品", "数量",
                    "总价", "购买日期"};

            for (int i = 0; i < headerArr.length; i++) {
                sheet1.addCell(new Label(i, 0, headerArr[i]));
            }
            int k = 1;
            for (int i = 0; i < orderList.size(); i++) {
                Order order = orderList.get(i);
                List<OrderItem> orderItems = order.getOrderItems();
                for (int j = 0; j < orderItems.size(); j++) {
                    sheet1.addCell(new Label(0, k, order.getOrderId()));
                    sheet1.addCell(new Label(1, k, orderItems.get(j).getOrderItemId()));
                    sheet1.addCell(new Label(2, k, order.getAgent().getName()));
                    sheet1.addCell(new Label(3, k, orderItems.get(j).getCustomer().getName()));
                    sheet1.addCell(new Label(4, k, orderItems.get(j).getGoods().getName()));
                    sheet1.addCell(new Label(5, k, String.valueOf(orderItems.get(j).getGoodsQuantity())));
                    sheet1.addCell(new Label(6, k, String.valueOf(orderItems.get(j).getOrderItemPrice())));
                    sheet1.addCell(new Label(7, k, dateFormat.format(orderItems.get(j).getCreateAt())));

                    k++;
                }
            }
        }
        if (customerOrderList != null) {
            sheet2 = book.createSheet(" 客户订单 ", 1);
            String headerArr2[] = {"订单编号", "代理商", "顾客", "商品", "数量",
                    "总价", "购买日期"};
            for (int i = 0; i < headerArr2.length; i++) {
                sheet2.addCell(new Label(i, 0, headerArr2[i]));
            }
            for (int i = 0; i < customerOrderList.size(); i++) {
                sheet2.addCell(new Label(0, i + 1, customerOrderList.get(i).getOrderId()));
                if (customerOrderList.get(i).getAgent() != null) {
                    sheet2.addCell(new Label(1, i + 1, customerOrderList.get(i).getAgent().getName()));
                } else {
                    sheet2.addCell(new Label(1, i + 1, ""));
                }
                sheet2.addCell(new Label(2, i + 1, customerOrderList.get(i).getReceiverName()));
                sheet2.addCell(new Label(3, i + 1, customerOrderList.get(i).getGoods().getName()));
                sheet2.addCell(new Label(4, i + 1, String.valueOf(customerOrderList.get(i).getQuantity())));
                sheet2.addCell(new Label(5, i + 1, String.valueOf(customerOrderList.get(i).getTotalPrice())));
                sheet2.addCell(new Label(6, i + 1, dateFormat.format(customerOrderList.get(i).getCreateAt())));
            }

        }
        // 写入数据并关闭文件
        book.write();
        book.close();
        os.flush();
        os.close();
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/expressOne/{expressId}")
    @ResponseBody
    public ResultData expressOne(@PathVariable("expressId") String expressId, @RequestBody ExpressForm form, HttpServletRequest request) {
        ResultData resultData = new ResultData();
        List<Express> itemForms = form.getExpressItem();
        String expressNumber = form.getExpressNumber();
        Iterator<Express> iter = itemForms.iterator();
        while (iter.hasNext()) {
            Express item = iter.next();
            String id = item.getExpressId();
            if (expressId.equals(id)) {
                String linkId = item.getLinkId();
                if (!StringUtils.isEmpty(linkId) && linkId.startsWith("ORI")) {
                    Express4Agent express = new Express4Agent(expressNumber, item.getSenderName(), item.getSenderPhone(), item.getSenderAddress(), item.getReceiverName(), item.getReceiverPhone(), item.getReceiverAddress(), item.getGoodsName());
                    OrderItem temp = new OrderItem();
                    Map<String, Object> condition = new HashMap<>();
                    condition.put("orderItemId", linkId);
                    ResultData fetchResponse = orderService.fetchOrderItem(condition);
                    if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        temp = ((List<OrderItem>) fetchResponse.getData()).get(0);
                        temp.setStatus(OrderItemStatus.SHIPPED);
                        express.setItem(temp);
                        expressService.createExpress(express);
                        orderService.updateOrderItem(temp);
                        condition.clear();
                        condition.put("orderId", temp.getOrder().getOrderId());
                        fetchResponse = orderService.fetchOrder(condition);
                        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                            Order order = ((List<Order>) fetchResponse.getData()).get(0);
                            List<OrderItem> items = order.getOrderItems();
                            boolean flag = true;
                            for (OrderItem orderItem : items) {
                                if (orderItem.getStatus() != OrderItemStatus.SHIPPED) {
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag) {
                                order.setStatus(OrderStatus.FULLY_SHIPMENT);
                                orderService.modifyOrder(order);
                            } else {
                                order.setStatus(OrderStatus.PATIAL_SHIPMENT);
                                orderService.modifyOrder(order);
                            }
                            Subject subject = SecurityUtils.getSubject();
                            User user = (User) subject.getPrincipal();
                            if (user == null) {
                                resultData.setData(itemForms);
                                return resultData;
                            }
                            Admin admin = user.getAdmin();
                            BackOperationLog backOperationLog = new BackOperationLog(
                                    admin.getUsername(), toolService.getIP((HttpServletRequest) request), "管理员" + admin.getUsername() + "单独发货，快递单号是：" + expressNumber + ",订单号是：" + order.getOrderId());
                            logService.createbackOperationLog(backOperationLog);
                        }

                    }

                } else if (linkId.startsWith("CUO")) {
                    Express4Customer express = new Express4Customer(expressNumber, item.getSenderName(), item.getSenderPhone(), item.getSenderAddress(), item.getReceiverName(), item.getReceiverPhone(), item.getReceiverAddress(), item.getGoodsName());
                    CustomerOrder temp = new CustomerOrder();
                    Map<String, Object> condition = new HashMap<>();
                    condition.put("orderId", linkId);
                    ResultData fetchResponse = orderService.fetchCustomerOrder(condition);
                    if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        temp = ((List<CustomerOrder>) fetchResponse.getData()).get(0);
                        temp.setStatus(OrderItemStatus.SHIPPED);
                        express.setOrder(temp);
                        expressService.createExpress(express);
                        orderService.updateCustomerOrder(temp);
                        Subject subject = SecurityUtils.getSubject();
                        User user = (User) subject.getPrincipal();
                        if (user == null) {
                            resultData.setData(itemForms);
                            return resultData;
                        }
                        Admin admin = user.getAdmin();
                        BackOperationLog backOperationLog = new BackOperationLog(
                                admin.getUsername(), toolService.getIP((HttpServletRequest) request), "管理员" + admin.getUsername() + "单独发货，快递单号是：" + expressNumber + ",订单号是：" + temp.getOrderId());
                        logService.createbackOperationLog(backOperationLog);
                    }
                }
                iter.remove();
                break;
            }
        }
        resultData.setData(itemForms);
        return resultData;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orderItem/{orderId}")
    @ResponseBody
    public ResultData overviewOrderItem(@PathVariable("orderId") String orderId) {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        ResultData orderData = orderService.fetchOrder(condition);
        Order order = ((List<Order>) orderData.getData()).get(0);
        double totalPrices = order.getPrice();
        Map<String, Object> goods_quantity_Map = new HashMap<>();
        for (OrderItem item : order.getOrderItems()) {
            String goodsName = item.getGoods().getName();
            int goodsQuantity = item.getGoodsQuantity();
            if (goods_quantity_Map.containsKey(goodsName)) {
                goods_quantity_Map
                        .put(goodsName,
                                ((int) goods_quantity_Map.get(goodsName) + goodsQuantity));
            } else {
                goods_quantity_Map.put(goodsName, goodsQuantity);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("totalPrices", totalPrices);
        result.put("goods_quantity_Map", goods_quantity_Map);
        resultData.setData(result);
        return resultData;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orderItem2/{orderId}")
    @ResponseBody
    public ResultData overviewOrderItem2(@PathVariable("orderId") String orderId) {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        ResultData orderData = orderService.fetchOrder(condition);
        Order order = ((List<Order>) orderData.getData()).get(0);
        List<OrderItem> orderItems = order.getOrderItems();
        Iterator<OrderItem> iterator = orderItems.iterator();
        while (iterator.hasNext()) {
            OrderItem orderItem = iterator.next();
            if (orderItem.getStatus() != OrderItemStatus.PAYED) {
                iterator.remove();
            }
        }
        order.setOrderItems(orderItems);
        double totalPrices = order.getPrice();
        Map<String, Object> goods_quantity_Map = new HashMap<>();
        for (OrderItem item : order.getOrderItems()) {
            String goodsName = item.getGoods().getName();
            int goodsQuantity = item.getGoodsQuantity();
            if (goods_quantity_Map.containsKey(goodsName)) {
                goods_quantity_Map
                        .put(goodsName,
                                ((int) goods_quantity_Map.get(goodsName) + goodsQuantity));
            } else {
                goods_quantity_Map.put(goodsName, goodsQuantity);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("totalPrices", totalPrices);
        result.put("goods_quantity_Map", goods_quantity_Map);
        resultData.setData(result);
        return resultData;
    }

    //intended to replace the two method above
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/{orderId}/item}")
    public ResultData viewItem(@PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        List<Integer> status = new ArrayList<>(Arrays.asList(OrderStatus.PAYED.getCode(), OrderStatus.PATIAL_SHIPMENT.getCode(), OrderStatus.FULLY_SHIPMENT.getCode(), OrderStatus.FINISHIED.getCode()));
        condition.put("status", status);
        ResultData response = orderService.fetchOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Order order = ((List<Order>) response.getData()).get(0);

        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/customerOrder/{orderId}")
    @ResponseBody
    public ResultData overviewCustomerOrder(@PathVariable("orderId") String orderId) {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        ResultData queryData = orderService.fetchCustomerOrder(condition);
        if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            CustomerOrder order = ((List<CustomerOrder>) queryData.getData()).get(0);
            resultData.setData(order);
            return resultData;
        }
        return resultData;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/statistics")
    @ResponseBody
    public ResultData statistics() {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(2);
        status.add(3);
        condition.put("status", status);
        List<Order> orderList = (List<Order>) orderService
                .fetchOrder(condition).getData();


        condition.clear();
        status.clear();
        status.add(1);
        condition.put("status", status);
        List<CustomerOrder> customerOrderList = (List<CustomerOrder>) orderService
                .fetchCustomerOrder(condition).getData();

        Map<String, Object> dataMap = new HashMap<>();
        List<Order> orderList2 = new ArrayList<>();
        if (orderList != null) {
            for (Order order : orderList) {
                List<OrderItem> orderItems = order.getOrderItems();
                Iterator<OrderItem> iterator = orderItems.iterator();
                while (iterator.hasNext()) {
                    OrderItem orderItem = iterator.next();
                    if (orderItem.getStatus() != OrderItemStatus.PAYED) {
                        iterator.remove();
                    }
                }
                order.setOrderItems(orderItems);
                orderList2.add(order);
            }
        }

        dataMap.put("orderList", orderList2);
        dataMap.put("customerOrderList", customerOrderList);
        resultData.setData(dataMap);

        return resultData;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statistics")
    public ModelAndView orderStatistics() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/statistics");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/save")
    public ModelAndView save() {
        ModelAndView view = new ModelAndView();
        view.setViewName("");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cancel/{orderId}")
    public ModelAndView cancelOrder(@PathVariable("orderId") String orderId,
                                    RedirectAttributes attr) {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            WechatConfig.oauthWechat(view, "/agent/login");
            view.setViewName("/agent/login");
            return view;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("orderId", orderId);
        ResultData orderFetchData = orderService.fetchOrder(condition);
        if (orderFetchData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "取消失败",
                    "/agent/order/manage/0");
            attr.addFlashAttribute("prompt", prompt);
            view.setViewName("redirect:/agent/prompt");
            return view;
        }
        Order order = ((List<Order>) orderFetchData.getData()).get(0);
        order.setBlockFlag(true);
        ResultData modifyFetchData = orderService.cancel(order);
        if (modifyFetchData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "取消失败",
                    "/agent/order/manage/0");
            attr.addFlashAttribute("prompt", prompt);
            view.setViewName("redirect:/agent/prompt");
            return view;
        }
        Prompt prompt = new Prompt("提示", "成功", "/agent/order/manage/0");
        attr.addFlashAttribute("prompt", prompt);
        view.setViewName("redirect:/agent/prompt");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/modify/{type}")
    public ModelAndView modifyOrder(@Valid OrderItemForm form,
                                    BindingResult result, RedirectAttributes attr,
                                    @PathVariable("type") String type, String openId) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/order/list/0");
            return view;
        }
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            WechatConfig.oauthWechat(view, "/agent/login");
            view.setViewName("/agent/login");
            return view;
        }
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        int length = form.getCustomerId().length;
        Order order = new Order();
        order.setAgent(user.getAgent());
        // 构造订单和订单项
        double total_price = 0;
        for (int i = 0; i < length; i++) {
            String goodsId = form.getGoodsId()[i];// 商品ID
            String customerId = form.getCustomerId()[i];// 顾客ID
            String orderItemId = form.getOrderItemId()[i];// 订单项ID
            String address = form.getAddress()[i];
            String description = "";
            if (!StringUtils.isEmpty(form.getDescription()) && form.getDescription().length >= i + 1) {
                description = form.getDescription()[i];
            }
            int goodsQuantity = Integer.parseInt(form.getGoodsQuantity()[i]);// 商品数量
            double orderItemPrice = 0;// OrderItem总价
            Map<String, Object> goodsCondition = new HashMap<>();// 查询商品价格
            goodsCondition.put("goodsId", goodsId);
            ResultData goodsData = commodityService
                    .fetchGoods4Customer(goodsCondition);
            Goods4Agent goods = null;
            if (goodsData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<Goods4Agent> goodsList = (List) goodsData.getData();
                if (goodsList.size() != 1) {
                    Prompt prompt = new Prompt(PromptCode.WARNING, "提示",
                            "商品不唯一或未找到", "/agent/order/place");
                    attr.addFlashAttribute("prompt", prompt);
                    view.setViewName("redirect:/agent/prompt");
                    return view;
                }
                goods = goodsList.get(0);
            } else {
                Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "商品信息异常",
                        "/agent/order/place");
                attr.addFlashAttribute("prompt", prompt);
                view.setViewName("redirect:/agent/prompt");
                return view;
            }
            orderItemPrice = goods.getAgentPrice() * goodsQuantity;// 得到一个OrderItem的总价
            total_price += orderItemPrice;// 累加金额
            OrderItem orderItem = new OrderItem(customerId, goodsId,
                    goodsQuantity, orderItemPrice, address, description);// 构造OrderItem
            orderItem.setOrderItemId(orderItemId);// 传入OrderItemID
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);// 构造Order
        order.setPrice(total_price);
        order.setOrderId(form.getOrderId());
        switch (type) {
            case "save":
                order.setStatus(OrderStatus.SAVED);
                break;
            case "submit":
                order.setStatus(OrderStatus.SUBMITTED);
                break;
            default:
                order.setStatus(OrderStatus.SAVED);
        }

        ResultData fetchResponse = orderService.modifyOrder(order);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (type.equals("save")) {
                Prompt prompt = new Prompt("提示", "保存成功",
                        "/agent/order/manage/0");
                attr.addFlashAttribute("prompt", prompt);
                view.setViewName("redirect:/agent/prompt");
            } else if (type.equals("submit")) {
                attr.addFlashAttribute("openId", openId);
                view.setViewName("redirect:/order/pay/" + order.getOrderId());
            }
            return view;
        }
        Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "失败",
                "/agent/order/manage/0");
        attr.addFlashAttribute("prompt", prompt);
        view.setViewName("redirect:/agent/prompt");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pay/{orderId}")
    public ModelAndView place(@PathVariable("orderId") String orderId) {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            WechatConfig.oauthWechat(view, "/agent/login");
            view.setViewName("/agent/login");
            return view;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("blockFlag", false);
        ResultData fetchAgentResponse = agentService.fetchAgent(condition);
        Agent agent = ((List<Agent>) fetchAgentResponse.getData()).get(0);
        condition.put("orderId", orderId);
        ResultData orderData = orderService.fetchOrder(condition);
        Order order = null;
        if (orderData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            order = ((List<Order>) orderData.getData()).get(0);
        } else {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "失败",
                    "/agent/order/manage/0");
            view.addObject("prompt", prompt);
            view.setViewName("redirect:/agent/prompt");
            return view;
        }
        condition.clear();
        condition.put("agentId", agent.getAgentId());
        Agent target = null;
        try {
            target = ((List<Agent>) agentService.fetchAgent(condition).getData()).get(0);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "失败",
                    "/agent/order/manage/0");
            view.addObject("prompt", prompt);
            view.setViewName("redirect:/agent/prompt");
            return view;
        }
        view.addObject("order", JSON.toJSON(order));
        view.addObject("agent", target);
        WechatConfig.oauthWechat(view, "agent/order/pay");
        view.setViewName("agent/order/pay");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cofferpay")
    public ModelAndView cofferPay(HttpServletRequest request,
                                  @Valid PayForm form, BindingResult result, RedirectAttributes attr) {
        ModelAndView view = new ModelAndView();
        String orderId = form.getOrderId();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            WechatConfig.oauthWechat(view, "/agent/login");
            view.setViewName("/agent/login");
            return view;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("orderId", orderId);
        ResultData orderData = orderService.fetchOrder(condition);
        Order order = null;
        try {
            order = ((List<Order>) orderData.getData()).get(0);
        } catch (Exception e) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "失败", null);
            attr.addFlashAttribute(prompt);
            view.setViewName("redirect:/agent/prompt");
            return view;
        }
        // 计算总价
        double totalPrice = order.getPrice();
        // 创建账单
        OrderBill orderBill = new OrderBill(totalPrice, "coffer",
                toolService.getIP(request), user.getAgent(), order);
        ResultData billData = billService.createOrderBill(orderBill, null);
        // 获取代理信息
        condition.clear();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("blockFlag", false);
        ResultData fetchAgentResponse = agentService.fetchAgent(condition);
        Agent agent = ((List<Agent>) fetchAgentResponse.getData()).get(0);
        if (billData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            ResultData cofferData = agentService.consume(agent, totalPrice);
            billService.updateOrderBill((OrderBill) billData.getData());
            if (cofferData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                // Order和OrderItem全部改成已付款
                order.setStatus(OrderStatus.PAYED);
                order.setCreateAt(new Timestamp(System.currentTimeMillis()));
                for (OrderItem orderItem : order.getOrderItems()) {
                    orderItem.setStatus(OrderItemStatus.PAYED);
                    orderItem.setCreateAt(new Timestamp(System.currentTimeMillis()));
                }
                ResultData payData = orderService.payOrder(order);
                if (payData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    Prompt prompt = new Prompt(PromptCode.SUCCESS, "付款成功",
                            "订单号：" + order.getOrderId() + "，请等待发货",
                            "/agent/order/manage/2");
                    attr.addFlashAttribute(prompt);
                    view.setViewName("redirect:/agent/prompt");
                    return view;
                }
            }
        }
        Prompt prompt = new Prompt(PromptCode.WARNING, "提示", "失败",
                "/agent/order/manage/1");
        attr.addFlashAttribute(prompt);
        view.setViewName("redirect:/agent/prompt");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/otherpay")
    public Charge otherPay(HttpServletRequest request) {
        Charge charge = new Charge();
        JSONObject params = toolService.getParams(request);
        Subject subject = SecurityUtils.getSubject();
        String clientIp = toolService.getIP(request);
        User user = (User) subject.getPrincipal();
        if (user == null) {
            return null;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("orderId", String.valueOf(params.get("order_id")));
        ResultData orderData = orderService.fetchOrder(condition);
        Order order = null;
        if (orderData.getResponseCode() == ResponseCode.RESPONSE_OK
                && orderData.getData() != null) {
            order = ((List<Order>) orderData.getData()).get(0);
        }
        OrderBill bill = new OrderBill(Double.parseDouble(String.valueOf(params
                .get("amount"))), String.valueOf(params.get("channel")),
                clientIp, user.getAgent(), order);
        ResultData createResponse = billService.createOrderBill(bill,
                params.getString("open_id"));
        if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            charge = (Charge) createResponse.getData();
        }
        return charge;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/customerpay")
    public Charge customerPay(HttpServletRequest request) {
        Charge charge = new Charge();
        JSONObject params = toolService.getParams(request);
        String clientIp = toolService.getIP(request);
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", String.valueOf(params.get("order_id")));
        ResultData orderData = orderService.fetchCustomerOrder(condition);
        CustomerOrder customerOrder = null;
        if (orderData.getResponseCode() == ResponseCode.RESPONSE_OK
                && orderData.getData() != null) {
            customerOrder = ((List<CustomerOrder>) orderData.getData()).get(0);
        }

        CustomerOrderBill bill = new CustomerOrderBill(
                Double.parseDouble(String.valueOf(params.get("amount"))),
                String.valueOf(params.get("channel")), clientIp, customerOrder);
        ResultData createResponse = billService.createCustomerOrderBill(bill,
                params.getString("open_id"));
        if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            charge = (Charge) createResponse.getData();
        }
        return charge;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/adminpay")
    public ResultData adminPay(HttpServletRequest request, String orderItemId) {
        ResultData result = new ResultData();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("管理员未登录");
            return result;
        }
        if (StringUtils.isEmpty(orderItemId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);

            result.setDescription("获取订单错误");
            return result;
        }
        Admin admin = user.getAdmin();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("orderItemId", orderItemId);
        ResultData fetchOrderItemData = orderService.fetchOrderItem(condition);
        if (fetchOrderItemData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(fetchOrderItemData.getResponseCode());
            result.setDescription("获取订单错误");
            return result;
        }
        OrderItem orderItem = ((List<OrderItem>) fetchOrderItemData.getData()).get(0);
        if (orderItem.getStatus() != OrderItemStatus.NOT_PAYED) {
            result.setResponseCode(fetchOrderItemData.getResponseCode());
            result.setDescription("订单已付款");
            return result;
        }
        condition.clear();
        condition.put("orderId", orderItem.getOrder().getOrderId());
        ResultData fetchOrderData = orderService.fetchOrder(condition);
        if (fetchOrderData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(fetchOrderData.getResponseCode());
            result.setDescription("获取订单错误");
            return result;
        }
        Order order = ((List<Order>) fetchOrderData.getData()).get(0);
        if (order.getOrderItems().size() == 1) {
            // 将Order和OrderItem变成已付款
            order.setStatus(OrderStatus.PAYED);
            order.setCreateAt(new Timestamp(System.currentTimeMillis()));
            orderItem = order.getOrderItems().get(0);
            orderItem.setStatus(OrderItemStatus.PAYED);
            orderItem.setCreateAt(new Timestamp(System.currentTimeMillis()));
            ResultData payData = orderService.payOrder(order);
            if (payData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(payData.getResponseCode());
                result.setDescription("付款失败");
                return result;
            }
        } else {
            order.setPrice(order.getPrice() - orderItem.getOrderItemPrice());
            ResultData updateOrderResponse = orderService.updateOrderLite(order);
            if (updateOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(updateOrderResponse.getResponseCode());
                result.setDescription("拆订单失败");
                return result;
            }
            order.setPrice(orderItem.getOrderItemPrice());
            order.setStatus(OrderStatus.PAYED);
            ResultData insertOrderResponse = orderService.createOrder(order);
            if (updateOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(updateOrderResponse.getResponseCode());
                result.setDescription("新建新订单失败");
                return result;
            }
            order = (Order) insertOrderResponse.getData();
            orderItem.setOrder(order);
            orderItem.setStatus(OrderItemStatus.PAYED);
            ResultData updateOrderItemResponse = orderService.updateOrderItem(orderItem);
            if (updateOrderItemResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(updateOrderItemResponse.getResponseCode());
                result.setDescription("订单项重置ID失败");
                return result;
            }
        }

        // 记录下单的admin
        BackOperationLog backOperationLog = new BackOperationLog(
                admin.getUsername(), toolService.getIP(request), "管理员" + admin.getUsername() + "将订单:"
                + orderItemId + "设置为已付款");
        ResultData createLogData = logService
                .createbackOperationLog(backOperationLog);
        if (createLogData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(createLogData.getResponseCode());
            result.setDescription("记录操作日志失败");
            return result;
        }
        return result;
    }

    /**
     * 确认签收
     *
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/shipment")
    public ResultData shipped(HttpServletRequest request, String orderId, String expressNo) {
        ResultData result = new ResultData();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未登录");
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        if (orderId.startsWith("ORI")) {
            condition.put("orderItemId", orderId);
            ResultData fetchOrderItemResponse = orderService.fetchOrderItem(condition);
            if (fetchOrderItemResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(fetchOrderItemResponse.getResponseCode());
                result.setDescription(fetchOrderItemResponse.getDescription());
                return result;
            }
            OrderItem orderItem = ((List<OrderItem>) fetchOrderItemResponse.getData()).get(0);
            if (orderItem.getStatus() != OrderItemStatus.PAYED) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("该状态订单无权发货");
                return result;
            }
            condition.clear();
            condition.put("customerId", orderItem.getCustomer().getCustomerId());
            ResultData fetchCustomerResponse = customerService.fetchCustomer(condition);
            if (fetchCustomerResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("发货客户信息获取失败");
                return result;
            }
            CustomerVo customer = ((List<CustomerVo>) fetchCustomerResponse.getData()).get(0);
            Express4Agent express = new Express4Agent(expressNo, PlatformConfig.getValue("sender_name"),
                    PlatformConfig.getValue("sender_phone"),
                    PlatformConfig.getValue("sender_address"), customer.getName(), customer.getPhone(), orderItem.getReceiveAddress(), orderItem.getGoods().getName());
            orderItem.setStatus(OrderItemStatus.SHIPPED);
            express.setItem(orderItem);
            ResultData createExpressResponse = expressService.createExpress(express);
            if (createExpressResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(createExpressResponse.getResponseCode());
                result.setDescription("创建快递单失败");
                return result;
            }
            ResultData updateOrderItemResponse = orderService.updateOrderItem(orderItem);
            if (updateOrderItemResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(updateOrderItemResponse.getResponseCode());
                result.setDescription(updateOrderItemResponse.getDescription());
                return result;
            }
            condition.clear();
            condition.put("orderId", orderItem.getOrder().getOrderId());
            ResultData fetchOrderResponse = orderService.fetchOrder(condition);
            if (fetchOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(fetchOrderResponse.getResponseCode());
                result.setDescription(fetchOrderResponse.getDescription());
                return result;
            }
            Order order = ((List<Order>) fetchOrderResponse.getData()).get(0);
            boolean allShipped = true;
            for (OrderItem item : order.getOrderItems()) {
                if (item.getStatus() == OrderItemStatus.NOT_PAYED || item.getStatus() == OrderItemStatus.PAYED) {
                    allShipped = false;
                    break;
                }
            }
            if (allShipped) {
                order.setStatus(OrderStatus.FULLY_SHIPMENT);
                ResultData updateOrderResponse = orderService.received(order);
                if (updateOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(updateOrderResponse.getResponseCode());
                    result.setDescription(updateOrderResponse.getDescription());
                    return result;
                }
            } else {
                order.setStatus(OrderStatus.PATIAL_SHIPMENT);
                ResultData updateOrderResponse = orderService.received(order);
                if (updateOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(updateOrderResponse.getResponseCode());
                    result.setDescription(updateOrderResponse.getDescription());
                    return result;
                }
            }
        } else if (orderId.startsWith("CUO")) {
            condition.put("orderId", orderId);
            ResultData fetchCustomerOrderResponse = orderService.fetchCustomerOrder(condition);
            if (fetchCustomerOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(fetchCustomerOrderResponse.getResponseCode());
                result.setDescription(fetchCustomerOrderResponse.getDescription());
                return result;
            }
            CustomerOrder customerOrder = ((List<CustomerOrder>) fetchCustomerOrderResponse.getData()).get(0);
            if (customerOrder.getStatus() != OrderItemStatus.PAYED) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("该状态订单无权发货");
                return result;
            }
            Express4Customer express = new Express4Customer(expressNo, PlatformConfig.getValue("sender_name"),
                    PlatformConfig.getValue("sender_phone"),
                    PlatformConfig.getValue("sender_address"), customerOrder.getReceiverName(), customerOrder.getReceiverPhone(), customerOrder.getReceiverAddress(), customerOrder.getGoods().getName());
            customerOrder.setStatus(OrderItemStatus.SHIPPED);
            express.setOrder(customerOrder);
            ResultData createExpressResponse = expressService.createExpress(express);
            if (createExpressResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(createExpressResponse.getResponseCode());
                result.setDescription("创建快递单失败");
                return result;
            }
            ResultData updateCustomerOrderResponse = orderService.updateCustomerOrder(customerOrder);
            if (updateCustomerOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(updateCustomerOrderResponse.getResponseCode());
                result.setDescription(updateCustomerOrderResponse.getDescription());
                return result;
            }
        } else if (orderId.startsWith("EOI")) {
            condition.put("orderId", orderId);
            ResultData fetchEventOrderResponse = eventService.fetchEventOrder(condition);
            if (fetchEventOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(fetchEventOrderResponse.getResponseCode());
                result.setDescription(fetchEventOrderResponse.getDescription());
                return result;
            }
            EventOrder eventOrder = ((List<EventOrder>) fetchEventOrderResponse.getData()).get(0);
            eventOrder.setStatus(OrderItemStatus.SHIPPED);
            ResultData updateEventOrderResponse = eventService.updateEventOrder(eventOrder);
            if (updateEventOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(updateEventOrderResponse.getResponseCode());
                result.setDescription(updateEventOrderResponse.getDescription());
                return result;
            }
        }
        // 记录确认发货的日志
        if (user.getAdmin() != null) {
            Admin admin = user.getAdmin();
            BackOperationLog backOperationLog = new BackOperationLog(
                    admin.getUsername(), toolService.getIP(request), "管理员" + admin.getUsername() + "将订单:"
                    + orderId + "设置为已发货");
            ResultData createLogData = logService
                    .createbackOperationLog(backOperationLog);
            if (createLogData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(createLogData.getResponseCode());
                result.setDescription("记录操作日志失败");
                return result;
            }
        }
        return result;
    }

    /**
     * 确认签收
     *
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/receive")
    public ResultData received(HttpServletRequest request, String orderId) {
        ResultData result = new ResultData();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未登录");
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        if (orderId.startsWith("ORI")) {
            condition.put("orderItemId", orderId);
            ResultData fetchOrderItemResponse = orderService.fetchOrderItem(condition);
            if (fetchOrderItemResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(fetchOrderItemResponse.getResponseCode());
                result.setDescription(fetchOrderItemResponse.getDescription());
                return result;
            }
            OrderItem orderItem = ((List<OrderItem>) fetchOrderItemResponse.getData()).get(0);
            if (orderItem.getStatus() != OrderItemStatus.PAYED && orderItem.getStatus() != OrderItemStatus.SHIPPED) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("该状态订单无权签收");
                return result;
            }
            orderItem.setStatus(OrderItemStatus.RECEIVED);
            ResultData updateOrderItemResponse = orderService.updateOrderItem(orderItem);
            if (updateOrderItemResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(updateOrderItemResponse.getResponseCode());
                result.setDescription(updateOrderItemResponse.getDescription());
                return result;
            }
            condition.clear();
            condition.put("orderId", orderItem.getOrder().getOrderId());
            ResultData fetchOrderResponse = orderService.fetchOrder(condition);
            if (fetchOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(fetchOrderResponse.getResponseCode());
                result.setDescription(fetchOrderResponse.getDescription());
                return result;
            }
            Order order = ((List<Order>) fetchOrderResponse.getData()).get(0);
            boolean allReceived = true;
            for (OrderItem item : order.getOrderItems()) {
                if (item.getStatus() == OrderItemStatus.NOT_PAYED || item.getStatus() == OrderItemStatus.PAYED || item.getStatus() == OrderItemStatus.SHIPPED) {
                    allReceived = false;
                    break;
                }
            }
            if (allReceived) {
                order.setStatus(OrderStatus.FINISHIED);
                ResultData updateOrderResponse = orderService.received(order);
                if (updateOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(updateOrderResponse.getResponseCode());
                    result.setDescription(updateOrderResponse.getDescription());
                    return result;
                }
            }
        } else if (orderId.startsWith("CUO")) {
            condition.put("orderId", orderId);
            ResultData fetchCustomerOrderResponse = orderService.fetchCustomerOrder(condition);
            if (fetchCustomerOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(fetchCustomerOrderResponse.getResponseCode());
                result.setDescription(fetchCustomerOrderResponse.getDescription());
                return result;
            }
            CustomerOrder customerOrder = ((List<CustomerOrder>) fetchCustomerOrderResponse.getData()).get(0);
            if (customerOrder.getStatus() != OrderItemStatus.PAYED && customerOrder.getStatus() != OrderItemStatus.SHIPPED) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("该状态订单无权签收");
                return result;
            }
            customerOrder.setStatus(OrderItemStatus.RECEIVED);
            ResultData updateCustomerOrderResponse = orderService.updateCustomerOrder(customerOrder);
            if (updateCustomerOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(updateCustomerOrderResponse.getResponseCode());
                result.setDescription(updateCustomerOrderResponse.getDescription());
                return result;
            }
        } else if (orderId.startsWith("EOI")) {
            condition.put("orderId", orderId);
            ResultData fetchEventOrderResponse = eventService.fetchEventOrder(condition);
            if (fetchEventOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(fetchEventOrderResponse.getResponseCode());
                result.setDescription(fetchEventOrderResponse.getDescription());
                return result;
            }
            EventOrder eventOrder = ((List<EventOrder>) fetchEventOrderResponse.getData()).get(0);
            eventOrder.setStatus(OrderItemStatus.RECEIVED);
            ResultData updateEventOrderResponse = eventService.updateEventOrder(eventOrder);
            if (updateEventOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(updateEventOrderResponse.getResponseCode());
                result.setDescription(updateEventOrderResponse.getDescription());
                return result;
            }
        }
        // 记录确认签收的日志
        if (user.getAdmin() != null) {
            Admin admin = user.getAdmin();
            BackOperationLog backOperationLog = new BackOperationLog(
                    admin.getUsername(), toolService.getIP(request), "管理员" + admin.getUsername() + "将订单:"
                    + orderId + "设置为已签收");
            ResultData createLogData = logService
                    .createbackOperationLog(backOperationLog);
            if (createLogData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(createLogData.getResponseCode());
                result.setDescription("记录操作日志失败");
                return result;
            }
        } else if (user.getAgent() != null) {
            common.sunshine.model.selling.agent.lite.Agent agent = user.getAgent();
            BackOperationLog backOperationLog = new BackOperationLog(
                    agent.getName(), toolService.getIP(request), "代理商" + agent.getName() + "将订单:"
                    + orderId + "设置为已签收");
            ResultData createLogData = logService
                    .createbackOperationLog(backOperationLog);
            if (createLogData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(createLogData.getResponseCode());
                result.setDescription("记录操作日志失败");
                return result;
            }
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/reimburse")
    public ResultData reimburse(HttpServletRequest request, String orderId) {
        ResultData result = new ResultData();

        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null || user.getAdmin() == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("您无权限进行订单退款操作");
        }
        Admin admin = user.getAdmin();
        BackOperationLog backOperationLog = new BackOperationLog(admin.getUsername(), toolService.getIP(request), "管理员" + admin.getUsername() + "发起对客户订单:" + orderId + "退款处理");
        logService.createbackOperationLog(backOperationLog);

        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("status", OrderItemStatus.PAYED.getCode());
        ResultData response = billService.fetchCustomerOrderBill(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("系统中未查询到相应的账单信息，请核实");
            backOperationLog = new BackOperationLog(admin.getUsername(), toolService.getIP(request), "管理员" + admin.getUsername() + "操作的:" + orderId + "退款失败,失败原因为:" + result.getDescription());
            logService.createbackOperationLog(backOperationLog);
            return result;
        }
        CustomerOrderBill bill = ((List<CustomerOrderBill>) response.getData()).get(0);
        condition.clear();
        condition.put("orderNo", bill.getBillId());
        response = chargeService.fectchCharge(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            backOperationLog = new BackOperationLog(admin.getUsername(), toolService.getIP(request), "管理员" + admin.getUsername() + "操作的:" + orderId + "退款失败,失败原因为:" + result.getDescription());
            logService.createbackOperationLog(backOperationLog);
            return result;
        }
        common.sunshine.model.selling.charge.Charge charge = ((List<common.sunshine.model.selling.charge.Charge>) response.getData()).get(0);
        response = chargeService.reimburse(charge);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }
        RefundBill refundBill = new RefundBill();
        refundBill.setBillId(bill.getBillId());
        refundBill.setRefundAmount(bill.getBillAmount());
        refundBill.setBillAmount(bill.getBillAmount());
        response = billService.createRefundBill(refundBill);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }
        condition.clear();
        condition.put("orderId", orderId);
        response = orderService.fetchCustomerOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            CustomerOrder current = ((List<CustomerOrder>) response.getData()).get(0);
            current.setStatus(OrderItemStatus.REFUNDING);
            orderService.updateCustomerOrder(current);
        }
        return result;
    }

    /**
     * 退货完成
     *
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/refunded")
    public ResultData refunded(HttpServletRequest request, String orderId) {
        ResultData result = new ResultData();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未登录");
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        if (orderId.startsWith("ORI")) {
            condition.put("orderItemId", orderId);
            ResultData fetchOrderItemResponse = orderService.fetchOrderItem(condition);
            if (fetchOrderItemResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(fetchOrderItemResponse.getResponseCode());
                result.setDescription(fetchOrderItemResponse.getDescription());
                return result;
            }
            OrderItem orderItem = ((List<OrderItem>) fetchOrderItemResponse.getData()).get(0);
            orderItem.setStatus(OrderItemStatus.REFUNDED);
            ResultData updateOrderItemResponse = orderService.updateOrderItem(orderItem);
            if (updateOrderItemResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(updateOrderItemResponse.getResponseCode());
                result.setDescription(updateOrderItemResponse.getDescription());
                return result;
            }
        } else if (orderId.startsWith("CUO")) {
            condition.put("orderId", orderId);
            ResultData fetchCustomerOrderResponse = orderService.fetchCustomerOrder(condition);
            if (fetchCustomerOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(fetchCustomerOrderResponse.getResponseCode());
                result.setDescription(fetchCustomerOrderResponse.getDescription());
                return result;
            }
            CustomerOrder customerOrder = ((List<CustomerOrder>) fetchCustomerOrderResponse.getData()).get(0);
            customerOrder.setStatus(OrderItemStatus.REFUNDED);
            ResultData updateCustomerOrderResponse = orderService.updateCustomerOrder(customerOrder);
            if (updateCustomerOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(updateCustomerOrderResponse.getResponseCode());
                result.setDescription(updateCustomerOrderResponse.getDescription());
                return result;
            }
        } else if (orderId.startsWith("EOI")) {
            condition.put("orderId", orderId);
            ResultData fetchEventOrderResponse = eventService.fetchEventOrder(condition);
            if (fetchEventOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(fetchEventOrderResponse.getResponseCode());
                result.setDescription(fetchEventOrderResponse.getDescription());
                return result;
            }
            EventOrder eventOrder = ((List<EventOrder>) fetchEventOrderResponse.getData()).get(0);
            eventOrder.setStatus(OrderItemStatus.REFUNDED);
            ResultData updateEventOrderResponse = eventService.updateEventOrder(eventOrder);
            if (updateEventOrderResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(updateEventOrderResponse.getResponseCode());
                result.setDescription(updateEventOrderResponse.getDescription());
                return result;
            }
        }
        // 记录退货 完成的日志
        if (user.getAdmin() != null) {
            Admin admin = user.getAdmin();
            BackOperationLog backOperationLog = new BackOperationLog(admin.getUsername(), toolService.getIP(request), "管理员" + admin.getUsername() + "将订单:" + orderId + "设置为已退货");
            ResultData createLogData = logService.createbackOperationLog(backOperationLog);
            if (createLogData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(createLogData.getResponseCode());
                result.setDescription("记录操作日志失败");
                return result;
            }
        } else if (user.getAgent() != null) {
            common.sunshine.model.selling.agent.lite.Agent agent = user.getAgent();
            BackOperationLog backOperationLog = new BackOperationLog(agent.getName(), toolService.getIP(request), "代理商" + agent.getName() + "将订单:" + orderId + "设置为已退货");
            ResultData createLogData = logService.createbackOperationLog(backOperationLog);
            if (createLogData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(createLogData.getResponseCode());
                result.setDescription("记录操作日志失败");
                return result;
            }
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/indent")
    public ModelAndView indent() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/indent");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/indent")
    public ResultData indent(@Valid TimeRangeForm form, BindingResult result) {
        ResultData resultData = new ResultData();
        boolean empty = true;
        if (result.hasErrors()) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return resultData;
        }
        List<Object> allList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("start", form.getStart());
        condition.put("end", form.getEnd());
        condition.put("blockFlag", false);
        List<Integer> status = new ArrayList<>(Arrays.asList(OrderItemStatus.PAYED.getCode(), OrderItemStatus.SHIPPED.getCode(), OrderItemStatus.RECEIVED.getCode()));
        condition.put("statusList", status);
        ResultData queryResponse = orderService.fetchOrderItem(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            empty = false;
            List<OrderItem> list = (List<OrderItem>) queryResponse.getData();
            allList.addAll(list);
        }
        condition.remove("statusList");
        condition.put("status", status);
        queryResponse = orderService.fetchCustomerOrder(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            empty = false;
            List<CustomerOrder> list = (List<CustomerOrder>) queryResponse.getData();
            allList.addAll(list);
        }
        queryResponse = eventService.fetchEventOrder(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            empty = false;
            List<EventOrder> list = (List<EventOrder>) queryResponse.getData();
            allList.addAll(list);
        }
        if (empty) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return resultData;
        }
        ResultData indentData = indentService.produceAll(allList);
        resultData.setData(indentData.getData());
        return resultData;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/list")
    public DataTablePage<Order> list(DataTableParam param) {
        DataTablePage<Order> result = new DataTablePage<Order>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("type", 1);
        condition.put("blockFlag", false);
        String params = param.getParams();
        if (!StringUtils.isEmpty(params)) {
            JSONObject jo = JSON.parseObject(params);
            if (jo.containsKey("start") && jo.containsKey("end")) {
                condition.put("start", (String) jo.get("start"));
                condition.put("end", (String) jo.get("end"));
            }
        }
        ResultData fetchResponse = orderService.fetchOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Order>) fetchResponse.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/indent/download/{fileName}/{tempFileName}")
    public String download(@PathVariable("fileName") String fileName, @PathVariable("tempFileName") String tempFileName, HttpServletRequest request,
                           HttpServletResponse response) throws UnsupportedEncodingException {
        // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        // 2.设置文件头：最后一个参数是设置下载文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("订单报表_" + fileName + ".xls", "utf-8"));
        OutputStream out;
        // 通过文件路径获得File对象
        String path = IndentController.class.getResource("/").getPath();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") >= 0) {
            path = path.substring(1);
        }
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/indent";
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(tempFileName + ".xls");
        File file = new File(sb.toString());
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream buff = new BufferedInputStream(fis);
            byte[] b = new byte[1024];// 相当于我们的缓存
            long k = 0;// 该值用于计算当前实际下载了多少字节

            // 3.通过response获取OutputStream对象(out)
            out = response.getOutputStream();
            // 开始循环下载
            while (k < file.length()) {
                int j = buff.read(b, 0, 1024);
                k += j;
                out.write(b, 0, j);
            }
            buff.close();
            fis.close();
            out.close();
            out.flush();
            file.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/view/{orderId}")
    public ModelAndView view(@PathVariable("orderId") String orderId) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        if (StringUtils.isEmpty(orderId)) {
            view.setViewName("redirect:/order/overview");
            return view;
        }
        if (orderId.startsWith("CUO")) {
            condition.put("orderId", orderId);
            ResultData response = orderService.fetchCustomerOrder(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                view.setViewName("redirect:/order/overview");
                return view;
            }
            view.addObject("order", ((List<CustomerOrder>) response.getData()).get(0));
        } else if (orderId.startsWith("ORI")) {
            condition.put("orderItemId", orderId);
            ResultData response = orderService.fetchOrderItem(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                view.setViewName("redirect:/order/overview");
                return view;
            }
            OrderItem item = ((List<OrderItem>) response.getData()).get(0);
            condition.clear();
            condition.put("orderId", item.getOrder().getOrderId());
            response = orderService.fetchOrder(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                view.setViewName("redirect:/order/overview");
                return view;
            }
            Order order = ((List<Order>) response.getData()).get(0);
            item.setOrder(order);
            view.addObject("order", item);
        } else {
            condition.put("orderId", orderId);
            ResultData response = eventService.fetchEventOrder(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                view.setViewName("redirect:/event/overview");
                return view;
            }
            view.addObject("order", ((List<EventOrder>) response.getData()).get(0));
        }
        view.setViewName("/backend/order/detail");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/summary")
    public ModelAndView summary() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/summary");
        return view;
    }

    /**
     * 查询订单交易笔数,item粒度
     */
    @RequestMapping(method = RequestMethod.POST, value = "/count")
    public ResultData orderItemSumCount(String status) {
        ResultData result = new ResultData();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未登录");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        if (user.getAgent() != null) {
            condition.put("agentId", user.getAgent().getAgentId());
        }
        if (!StringUtils.isEmpty(status)) {
            switch (status) {
                case "NOT_PAYED":
                    condition.put("status", OrderItemStatus.NOT_PAYED.getCode());
                    break;
                case "PAYED":
                    condition.put("status", OrderItemStatus.PAYED.getCode());
                    break;
                case "SHIPPED":
                    condition.put("status", OrderItemStatus.SHIPPED.getCode());
                    break;
                case "RECEIVED":
                    condition.put("status", OrderItemStatus.RECEIVED.getCode());
                    break;
                case "EXCHANGED":
                    condition.put("status", OrderItemStatus.EXCHANGED.getCode());
                    break;
                case "REFUNDING":
                    condition.put("status", OrderItemStatus.REFUNDING.getCode());
                    break;
                case "REFUNDED":
                    condition.put("status", OrderItemStatus.REFUNDED.getCode());
                    break;
                default:
                    break;
            }
        }
        ResultData fetchOrderItemSumResponse = orderService.fetchOrderItemSum(condition);
        if (fetchOrderItemSumResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(fetchOrderItemSumResponse.getResponseCode());
            result.setDescription("获取订单数据失败");
            return result;
        }
        List<OrderItemSum> orderItemSums = (List<OrderItemSum>) fetchOrderItemSumResponse.getData();
        result.setData(orderItemSums.size());
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search")
    public ResultData search(String phone, String data) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(phone)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询订单的电话号码不可为空!");
            return result;
        }
        List<OrderItemVo> content = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        //以电话号码为条件查询所有有效订单
        condition.put("blockFlag", false);
        //查询代理商的所有订单
        condition.put("phone", phone);
        List<Integer> status = new ArrayList<>(Arrays.asList(OrderItemStatus.PAYED.getCode(), OrderItemStatus.SHIPPED.getCode(), OrderItemStatus.RECEIVED.getCode()));
        condition.put("statusList", status);
        ResultData response = orderService.fetchOrderItem(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<OrderItem> list = (List<OrderItem>) response.getData();
            for (OrderItem item : list) {
                condition.clear();
                condition.put("orderId", item.getOrder().getOrderId());
                Order order = ((List<Order>) orderService.fetchOrder(condition).getData()).get(0);
                item.setOrder(order);
                OrderItemVo vo = new OrderItemVo(item);
                content.add(vo);
            }
        }
        //查询顾客订单
        condition.clear();
        status.clear();
        condition.put("receiverPhone", phone);
        condition.put("blockFlag", false);
        status.addAll(Arrays.asList(OrderItemStatus.PAYED.getCode(), OrderItemStatus.SHIPPED.getCode(), OrderItemStatus.RECEIVED.getCode()));
        condition.put("status", status);
        response = orderService.fetchCustomerOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CustomerOrder> list = (List<CustomerOrder>) response.getData();
            for (CustomerOrder item : list) {
                OrderItemVo vo = new OrderItemVo(item);
                content.add(vo);
            }
        }
        //查询活动订单
        condition.clear();
        status.clear();
        condition.put("doneePhone", phone);
        condition.put("blockFlag", false);
        status.addAll(Arrays.asList(OrderItemStatus.PAYED.getCode(), OrderItemStatus.SHIPPED.getCode(), OrderItemStatus.RECEIVED.getCode()));
        condition.put("status", status);
        response = eventService.fetchEventOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<EventOrder> list = (List<EventOrder>) response.getData();
            for (EventOrder item : list) {
                OrderItemVo vo = new OrderItemVo(item);
                content.add(vo);
            }
        }
        if (content.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未查询到相关的订单信息!");
        }
        result.setData(content);
        return result;
    }
}




