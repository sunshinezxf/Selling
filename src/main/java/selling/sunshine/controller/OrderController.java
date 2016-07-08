package selling.sunshine.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Charge;
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
import selling.sunshine.form.SortRule;
import selling.sunshine.model.*;
import selling.sunshine.model.express.Express;
import selling.sunshine.model.express.Express4Agent;
import selling.sunshine.model.express.Express4Customer;
import selling.sunshine.model.goods.Goods4Agent;
import selling.sunshine.model.goods.Goods4Customer;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.pagination.MobilePage;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.service.*;
import selling.sunshine.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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
    private AgentService agentService;

    @Autowired
    private BillService billService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private RefundService refundService;

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

//	@ResponseBody
//	@RequestMapping(method = RequestMethod.POST, value = "/overview")
//	public MobilePage<Order> overview(MobilePageParam param) {
//		MobilePage<Order> result = new MobilePage<>();
//		if (StringUtils.isEmpty(param)) {
//			return result;
//		}
//		Map<String, Object> condition = new HashMap<>();
//		List<Integer> status = new ArrayList<>();
//		switch (Integer.parseInt((String) param.getParams().get("status"))) {
//		case 0:
//			status.add(0);
//			break;
//		case 1:
//			status.add(1);
//			break;
//		case 2:
//			status.add(2);
//			status.add(3);
//			status.add(4);
//			break;
//		case 3:
//			status.add(5);
//			break;
//		}
//		condition.put("status", status);
//		ResultData fetchResponse = orderService.fetchOrder(condition, param);
//		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
//			result = (MobilePage<Order>) fetchResponse.getData();
//		}
//		return result;
//	}

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
        condition.put("status", status);
        ResultData fetchResponse = orderService.fetchOrder(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Order>) fetchResponse.getData();
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

    //	@ResponseBody
//	@RequestMapping(method = RequestMethod.POST, value = "/customerOrder/overview")
//	public MobilePage<CustomerOrder> customerOrderOverview(MobilePageParam param) {
//		MobilePage<CustomerOrder> result = new MobilePage<>();
//		if (StringUtils.isEmpty(param)) {
//			return result;
//		}
//		Map<String, Object> condition = new HashMap<>();
//		List<Integer> status = new ArrayList<>();
//		switch (Integer.parseInt((String) param.getParams().get("status"))) {
//		case 0:
//			status.add(0);
//			break;
//		case 1:
//			status.add(1);
//			break;
//		case 2:
//			status.add(2);
//			break;
//		case 3:
//			status.add(3);
//			break;
//		}
//		condition.put("status", status);
//		ResultData fetchResponse = orderService.fetchCustomerOrder(condition,
//				param);
//		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
//			result = (MobilePage<CustomerOrder>) fetchResponse.getData();
//		}
//		return result;
//	}
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
        condition.put("status", status);
        ResultData fetchResponse = orderService.fetchOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Order> list = (List<Order>) fetchResponse.getData();
            for (Order order : list) {
                for (OrderItem item : order.getOrderItems()) {
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
        ExpressComparator expressComparator = new ExpressComparator();
        Collections.sort(expresses, expressComparator);
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
        } else {
            Map<String, Object> condition = new HashMap<>();
            condition.put("orderId", customerOrderId);
            ResultData fetchResponse = orderService
                    .fetchCustomerOrder(condition);
            if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                CustomerOrder order = ((List<CustomerOrder>) orderService
                        .fetchCustomerOrder(condition).getData()).get(0);
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
        ExpressComparator expressComparator = new ExpressComparator();
        Collections.sort(expressList, expressComparator);
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
                ResultData fetchResponse =
                        orderService.fetchOrderItem(condition);
                if (fetchResponse.getResponseCode() ==
                        ResponseCode.RESPONSE_OK) {
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
            receiverPhone.setCellValue(expresseList.get(i).getReceiverPhone());
            Cell receiverAddress = current.createCell(13);
            receiverAddress.setCellValue(expresseList.get(i).getReceiverAddress());
            Cell goods = current.createCell(14);
            goods.setCellValue(expresseList.get(i).getGoodsName());
            Cell description = current.createCell(22);
            description.setCellValue(String.valueOf(expresseList.get(i).getGoodsQuantity()) + "盒");
            Cell orderNo = current.createCell(37);
            if (expresseList.get(i).getLinkId().startsWith("ORI")) {
                Express4Agent express = (Express4Agent) expresseList.get(i);
                orderNo.setCellValue(express.getItem().getOrderItemId());
            } else {
                Express4Customer express = (Express4Customer) expresseList.get(i);
                orderNo.setCellValue(express.getOrder().getOrderId());
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

    @RequestMapping(method = RequestMethod.GET, value = "/downloadOrderExcel")
    public String downloadOrderExcel(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException, WriteException {
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(2);
        condition.put("status", status);
        List<Order> orderList = (List<Order>) orderService
                .fetchOrder(condition).getData();

        condition.clear();
        status.clear();
        status.add(1);
        condition.put("status", status);
        List<CustomerOrder> customerOrderList = (List<CustomerOrder>) orderService
                .fetchCustomerOrder(condition).getData();
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

    @RequestMapping(method = RequestMethod.POST, value = "/statistics")
    @ResponseBody
    public ResultData statistics() {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(2);
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
        dataMap.put("orderList", orderList);
        dataMap.put("customerOrderList", customerOrderList);
        resultData.setData(dataMap);

        return resultData;
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
            int goodsQuantity = Integer.parseInt(form.getGoodsQuantity()[i]);// 商品数量
            double orderItemPrice = 0;// OrderItem总价
            Map<String, Object> goodsCondition = new HashMap<String, Object>();// 查询商品价格
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
                    goodsQuantity, orderItemPrice, address);// 构造OrderItem
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
            target = ((List<Agent>) agentService.fetchAgent(condition)
                    .getData()).get(0);
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
                for (OrderItem orderItem : order.getOrderItems()) {
                    orderItem.setStatus(OrderItemStatus.PAYED);
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
                "/agent/order/manage/2");
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
        Subject subject = SecurityUtils.getSubject();
        String clientIp = toolService.getIP(request);
        Map<String, Object> condition = new HashMap<String, Object>();
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

    @RequestMapping(method = RequestMethod.GET, value = "/adminpay/{orderId}")
    public ResultData adminPay(HttpServletRequest request,
                               @PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("管理员未登录");
            return result;
        }
        Admin admin = user.getAdmin();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("orderId", orderId);
        ResultData fetchOrderData = orderService.fetchOrder(condition);
        if (fetchOrderData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(fetchOrderData.getResponseCode());
            result.setDescription("获取订单错误");
            ;
            return result;
        }
        Order order = ((List<Order>) fetchOrderData.getData()).get(0);

        // 将Order和OrderItem变成已发货
        order.setStatus(OrderStatus.PAYED);
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setStatus(OrderItemStatus.PAYED);
        }
        ResultData payData = orderService.payOrder(order);
        if (payData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(payData.getResponseCode());
            result.setDescription("下单失败");
            return result;
        }

        // 记录下单的admin
        BackOperationLog backOperationLog = new BackOperationLog(
                admin.getUsername(), "管理员" + admin.getUsername() + "将订单:"
                + orderId + "设置为已付款");
        ResultData createLogData = logService
                .createbackOperationLog(backOperationLog);
        if (createLogData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(createLogData.getResponseCode());
            result.setDescription("记录操作日志失败");
            return result;
        }
        result.setData(payData.getData());
        return result;
    }

}

class ExpressComparator implements Comparator<Express> {

    @Override
    public int compare(Express e1, Express e2) {
        if (e1.getGoodsQuantity() >= e2.getGoodsQuantity()) {
            return -1;
        } else {
            return 1;
        }

    }

}
