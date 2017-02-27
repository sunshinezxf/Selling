package selling.sunshine.controller;

import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.customer.CustomerPhone;
import common.sunshine.model.selling.event.Event;
import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderType;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import common.sunshine.utils.SortRule;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.service.CustomerService;
import selling.sunshine.service.EventService;
import selling.sunshine.service.OrderService;
import selling.sunshine.service.StatementService;
import selling.sunshine.vo.customer.CustomerVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表下载接口
 * created by wxd
 */
@RequestMapping("/statement")
@RestController
public class StatementController {

    private Logger logger = LoggerFactory.getLogger(StatementController.class);

    @Autowired
    private StatementService statementService;

    @Autowired
    private EventService eventService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    /**
     * 跳转到报表专区界面
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/download")
    public ModelAndView download() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/statement/overview");
        return view;
    }

    /**
     * 根据活动ID下载某个赠送活动的相关信息excel
     * @param request
     * @param response
     * @param eventId
     * @return
     * @throws IOException
     * @throws RowsExceededException
     * @throws WriteException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/downloadEventExcel/{eventId}")
    public String downloadEventExcel(HttpServletRequest request, HttpServletResponse response,@PathVariable("eventId") String eventId)
            throws IOException, RowsExceededException, WriteException {

        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode("活动被赠送人信息统计表.xls", "utf-8"));
        OutputStream os = response.getOutputStream();
        WritableWorkbook book = null;
        WritableSheet sheet;
        book = jxl.Workbook.createWorkbook(os);
        String headerArr[] = {"活动名称", "被赠送人姓名", "被赠送人手机号", "被赠送人地址", "赠送人姓名", "赠送人手机号",
                "被赠送商品名称", "被赠送商品数量", "购买商品数量"};

        // 1.根据eventId查询对应的event
        Map<String, Object> condition = new HashMap<>();
        condition.put("eventId", eventId);
        ResultData queryData = new ResultData();
        queryData = eventService.fetchEvent(condition);
        if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Event event=((List<Event>)queryData.getData()).get(0);
            sheet = book.createSheet(event.getTitle(), 0);
            for (int h = 0; h < headerArr.length; h++) {
                sheet.addCell(new Label(h, 0, headerArr[h]));
            }
            // 2.对每一个event，统计被赠送人的个人信息、赠送信息和购买信息
            if (event.getEventId().startsWith("GEV")){ //赠送活动
                condition.clear();
                condition.put("eventId", event.getEventId());
                condition.put("status", 2);
                queryData = eventService.fetchEventApplication(condition);
                if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    List<EventApplication> applications = (List<EventApplication>) queryData.getData();
                    int i = 0;
                    for (EventApplication eventApplication : applications) {
                        i++;
                        condition.clear();
                        condition.put("applicationId", eventApplication.getApplicationId());
                        queryData = eventService.fetchEventOrder(condition);
                        if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                            EventOrder eventOrder = ((List<EventOrder>) queryData.getData()).get(0);
                            condition.clear();
                            condition.put("phone", eventOrder.getDoneePhone());
                            queryData = customerService.fetchCustomerPhone(condition);
                            //在顾客电话表中有记录证明其有购买记录
                            if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                CustomerPhone customerPhone = ((List<CustomerPhone>) queryData.getData()).get(0);
                                Customer customer = customerPhone.getCustomer();
                                condition.clear();
                                condition.put("customerId", customer.getCustomerId());
                                //  condition.put("blockFlag", false);
                                ResultData fetchCustomerResponse = customerService.fetchCustomer(condition);
                                CustomerVo customerDetail = ((List<CustomerVo>) fetchCustomerResponse.getData()).get(0);
                                //以下是查找该客户最近一次的购买订单，顺便统计该客户的各商品购买盒数
                                Map<String, Object[]> goodsMap = new HashMap<String, Object[]>();//商品ID->(商品name, 购买数量quantity)
                                String phone = customerPhone.getPhone();
                                condition.clear();
                                condition.put("customerId", customer.getCustomerId());
                                List<Integer> statusList = new ArrayList<>();
                                statusList.add(1);
                                statusList.add(2);
                                statusList.add(3);
                                statusList.add(4);
                                statusList.add(5);
                                statusList.add(6);
                                condition.put("statusList", statusList);
                                condition.put("blockFlag", false);
                                List<SortRule> orderBy = new ArrayList<>();
                                orderBy.add(new SortRule("create_time", "desc"));
                                condition.put("sort", orderBy);
                                ResultData fetchOrderItemResponse = orderService.fetchOrderItem(condition);
                                if (fetchOrderItemResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                    //分商品统计购买数量
                                    for (OrderItem orderItem : (List<OrderItem>) fetchOrderItemResponse.getData()) {
                                        if (goodsMap.containsKey(orderItem.getGoods().getGoodsId())) {
                                            Object[] goodsInfo = goodsMap.get(orderItem.getGoods().getGoodsId());
                                            if (orderItem.getOrder().getType() != OrderType.GIFT) {
                                                goodsInfo[1] = (Integer) goodsInfo[1] + orderItem.getGoodsQuantity();
                                            }
                                        } else {
                                            Object[] goodsInfo = new Object[2];
                                            goodsInfo[0] = orderItem.getGoods().getName();
                                            if (orderItem.getOrder().getType() != OrderType.GIFT) {
                                                goodsInfo[1] = orderItem.getGoodsQuantity();
                                            } else {
                                                goodsInfo[1] = Integer.valueOf(0);
                                            }
                                            goodsMap.put(orderItem.getGoods().getGoodsId(), goodsInfo);
                                        }
                                    }

                                }
                                condition.clear();
                                condition.put("receiverPhone", phone);
                                if (customerDetail.getAgent()!=null) {
                                    condition.put("agentId", customerDetail.getAgent().getAgentId());
                                }
                                condition.put("blockFlag", false);
                                condition.put("status", statusList);
                                condition.put("sort", orderBy);
                                ResultData fetchCustomerOrderResponse = orderService.fetchCustomerOrder(condition);
                                if (fetchCustomerOrderResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                    //分商品统计购买数量
                                    for (CustomerOrder customerOrder : (List<CustomerOrder>) fetchCustomerOrderResponse.getData()) {
                                        if (goodsMap.containsKey(customerOrder.getGoods().getGoodsId())) {
                                            Object[] goodsInfo = goodsMap.get(customerOrder.getGoods().getGoodsId());
                                            goodsInfo[1] = (Integer) goodsInfo[1] + customerOrder.getQuantity();
                                        } else {
                                            Object[] goodsInfo = new Object[2];
                                            goodsInfo[0] = customerOrder.getGoods().getName();
                                            goodsInfo[1] = customerOrder.getQuantity();
                                            goodsMap.put(customerOrder.getGoods().getGoodsId(), goodsInfo);
                                        }
                                    }
                                }
                                for (String goodsId : goodsMap.keySet()) {
                                    Object[] goodsObject = goodsMap.get(goodsId);
                                    Goods4Agent goods4Agent = new Goods4Agent();
                                    goods4Agent.setGoodsId(goodsId);
                                    sheet.addCell(new Label(0, i, event.getTitle()));
                                    sheet.addCell(new Label(1, i, eventOrder.getDoneeName()));
                                    sheet.addCell(new Label(2, i, eventOrder.getDoneePhone()));
                                    sheet.addCell(new Label(3, i, eventOrder.getDoneeAddress()));
                                    sheet.addCell(new Label(4, i, eventApplication.getDonorName()));
                                    sheet.addCell(new Label(5, i, eventApplication.getDonorPhone()));
                                    if (goodsId.equals(eventOrder.getGoods().getGoodsId())) {
                                        sheet.addCell(new Label(6, i, eventOrder.getGoods().getName()));
                                        sheet.addCell(new Label(7, i, String.valueOf(eventOrder.getQuantity())));
                                        sheet.addCell(new Label(8, i, ((Integer) ((goodsMap.get(goodsId))[1])).toString()));
                                    } else {
                                        sheet.addCell(new Label(6, i, (String) goodsObject[0]));
                                        sheet.addCell(new Label(7, i, "0"));
                                        sheet.addCell(new Label(8, i, ((Integer) ((goodsMap.get(goodsId))[1])).toString()));
                                    }
                                    i++;
                                }
                                i--;
                            } else {
                                //否则，没有购买，购买数量为0
                                sheet.addCell(new Label(0, i, event.getTitle()));
                                sheet.addCell(new Label(1, i, eventOrder.getDoneeName()));
                                sheet.addCell(new Label(2, i, eventOrder.getDoneePhone()));
                                sheet.addCell(new Label(3, i, eventOrder.getDoneeAddress()));
                                sheet.addCell(new Label(4, i, eventApplication.getDonorName()));
                                sheet.addCell(new Label(5, i, eventApplication.getDonorPhone()));
                                sheet.addCell(new Label(6, i, eventOrder.getGoods().getName()));
                                sheet.addCell(new Label(7, i, String.valueOf(eventOrder.getQuantity())));
                                sheet.addCell(new Label(8, i, "0"));
                            }
                        }
                    }
                }
                }else if (event.getEventId().startsWith("PRE")) { //满赠活动
                    condition.clear();
                    condition.put("eventId", event.getEventId());
                    queryData = eventService.fetchEventOrder(condition);
                    if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        List<EventOrder> eventOrderList = (List<EventOrder>) queryData.getData();
                        int i=0;
                        for (EventOrder eventOrder:eventOrderList){
                            i++;
                            condition.clear();
                            condition.put("phone", eventOrder.getDoneePhone());
                            queryData = customerService.fetchCustomerPhone(condition);
                            //在顾客电话表中有记录证明其有购买记录
                            if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                CustomerPhone customerPhone = ((List<CustomerPhone>) queryData.getData()).get(0);
                                Customer customer = customerPhone.getCustomer();
                                condition.clear();
                                condition.put("customerId", customer.getCustomerId());
                                //  condition.put("blockFlag", false);
                                ResultData fetchCustomerResponse = customerService.fetchCustomer(condition);
                                CustomerVo customerDetail = ((List<CustomerVo>) fetchCustomerResponse.getData()).get(0);
                                //以下是查找该客户最近一次的购买订单，顺便统计该客户的各商品购买盒数
                                Map<String, Object[]> goodsMap = new HashMap<String, Object[]>();//商品ID->(商品name, 购买数量quantity)
                                String phone = customerPhone.getPhone();
                                condition.clear();
                                condition.put("customerId", customer.getCustomerId());
                                List<Integer> statusList = new ArrayList<>();
                                statusList.add(1);
                                statusList.add(2);
                                statusList.add(3);
                                statusList.add(4);
                                statusList.add(5);
                                statusList.add(6);
                                condition.put("statusList", statusList);
                                condition.put("blockFlag", false);
                                List<SortRule> orderBy = new ArrayList<>();
                                orderBy.add(new SortRule("create_time", "desc"));
                                condition.put("sort", orderBy);
                                ResultData fetchOrderItemResponse = orderService.fetchOrderItem(condition);
                                if (fetchOrderItemResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                    //分商品统计购买数量
                                    for (OrderItem orderItem : (List<OrderItem>) fetchOrderItemResponse.getData()) {
                                        if (goodsMap.containsKey(orderItem.getGoods().getGoodsId())) {
                                            Object[] goodsInfo = goodsMap.get(orderItem.getGoods().getGoodsId());
                                            if (orderItem.getOrder().getType() != OrderType.GIFT) {
                                                goodsInfo[1] = (Integer) goodsInfo[1] + orderItem.getGoodsQuantity();
                                            }
                                        } else {
                                            Object[] goodsInfo = new Object[2];
                                            goodsInfo[0] = orderItem.getGoods().getName();
                                            if (orderItem.getOrder().getType() != OrderType.GIFT) {
                                                goodsInfo[1] = orderItem.getGoodsQuantity();
                                            } else {
                                                goodsInfo[1] = Integer.valueOf(0);
                                            }
                                            goodsMap.put(orderItem.getGoods().getGoodsId(), goodsInfo);
                                        }
                                    }

                                }
                                condition.clear();
                                condition.put("receiverPhone", phone);
                                if (customerDetail.getAgent()!=null) {
                                    condition.put("agentId", customerDetail.getAgent().getAgentId());
                                }
                                condition.put("blockFlag", false);
                                condition.put("status", statusList);
                                condition.put("sort", orderBy);
                                ResultData fetchCustomerOrderResponse = orderService.fetchCustomerOrder(condition);
                                if (fetchCustomerOrderResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                    //分商品统计购买数量
                                    for (CustomerOrder customerOrder : (List<CustomerOrder>) fetchCustomerOrderResponse.getData()) {
                                        if (goodsMap.containsKey(customerOrder.getGoods().getGoodsId())) {
                                            Object[] goodsInfo = goodsMap.get(customerOrder.getGoods().getGoodsId());
                                            goodsInfo[1] = (Integer) goodsInfo[1] + customerOrder.getQuantity();
                                        } else {
                                            Object[] goodsInfo = new Object[2];
                                            goodsInfo[0] = customerOrder.getGoods().getName();
                                            goodsInfo[1] = customerOrder.getQuantity();
                                            goodsMap.put(customerOrder.getGoods().getGoodsId(), goodsInfo);
                                        }
                                    }
                                }
                                for (String goodsId : goodsMap.keySet()) {
                                    Object[] goodsObject = goodsMap.get(goodsId);
                                    Goods4Agent goods4Agent = new Goods4Agent();
                                    goods4Agent.setGoodsId(goodsId);
                                    sheet.addCell(new Label(0, i, event.getTitle()));
                                    sheet.addCell(new Label(1, i, eventOrder.getDoneeName()));
                                    sheet.addCell(new Label(2, i, eventOrder.getDoneePhone()));
                                    sheet.addCell(new Label(3, i, eventOrder.getDoneeAddress()));
                                    sheet.addCell(new Label(4, i, ""));
                                    sheet.addCell(new Label(5, i, ""));
                                    if (goodsId.equals(eventOrder.getGoods().getGoodsId())) {
                                        sheet.addCell(new Label(6, i, eventOrder.getGoods().getName()));
                                        sheet.addCell(new Label(7, i, String.valueOf(eventOrder.getQuantity())));
                                        sheet.addCell(new Label(8, i, ((Integer) ((goodsMap.get(goodsId))[1])).toString()));
                                    } else {
                                        sheet.addCell(new Label(6, i, (String) goodsObject[0]));
                                        sheet.addCell(new Label(7, i, "0"));
                                        sheet.addCell(new Label(8, i, ((Integer) ((goodsMap.get(goodsId))[1])).toString()));
                                    }
                                    i++;
                                }
                                i--;
                            } else {
                                //否则，没有购买，购买数量为0
                                sheet.addCell(new Label(0, i, event.getTitle()));
                                sheet.addCell(new Label(1, i, eventOrder.getDoneeName()));
                                sheet.addCell(new Label(2, i, eventOrder.getDoneePhone()));
                                sheet.addCell(new Label(3, i, eventOrder.getDoneeAddress()));
                                sheet.addCell(new Label(4, i, ""));
                                sheet.addCell(new Label(5, i, ""));
                                sheet.addCell(new Label(6, i, eventOrder.getGoods().getName()));
                                sheet.addCell(new Label(7, i, String.valueOf(eventOrder.getQuantity())));
                                sheet.addCell(new Label(8, i, "0"));
                            }
                        }
                    }
                }
            }

        // 写入数据并关闭文件
        book.write();
        book.close();
        os.flush();
        os.close();
        return null;
    }

    /**
     * 下载日常赠送的相关信息excel
     * @param request
     * @param response
     * @param start
     * @param end
     * @return
     * @throws IOException
     * @throws RowsExceededException
     * @throws WriteException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/downloadGiftExcel")
    public String downloadGiftExcel(HttpServletRequest request, HttpServletResponse response, String start, String end) throws IOException, RowsExceededException, WriteException {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("blockFlag", false);
        if(!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)){
        	condition.put("start", start);
        	condition.put("end", end);
        }
        condition.put("type", 1);
        ResultData fetchOrderResponse = orderService.fetchOrder(condition);
        List<Order> orders = (List<Order>) fetchOrderResponse.getData();
        Map<Customer, Map<Goods4Agent, Integer>> giftInfo = new HashMap<Customer, Map<Goods4Agent, Integer>>();
        if (orders != null) {
            for (Order order : orders) {
                if (order.getOrderItems().isEmpty()) {
                    continue;
                }
                for (OrderItem orderItem : order.getOrderItems()) {
                    Customer customer = orderItem.getCustomer();
                    Goods4Agent goods4Agent = orderItem.getGoods();
                    if (giftInfo.containsKey(customer)) {
                        Map<Goods4Agent, Integer> perGoodsInfo = giftInfo.get(customer);
                        perGoodsInfo.put(goods4Agent, (perGoodsInfo.containsKey(goods4Agent) ? perGoodsInfo.get(goods4Agent) : 0) + orderItem.getGoodsQuantity());
                    } else {
                        Map<Goods4Agent, Integer> perGoodsInfo = new HashMap<Goods4Agent, Integer>();
                        perGoodsInfo.put(goods4Agent, orderItem.getGoodsQuantity());
                        giftInfo.put(customer, perGoodsInfo);
                    }
                }
            }
        }

        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode("赠送信息.xls", "utf-8"));
        OutputStream os = response.getOutputStream();
        WritableWorkbook book = null;
        WritableSheet sheet1;
        //book = Workbook.createWorkbook(os);
        book = jxl.Workbook.createWorkbook(os);
        sheet1 = book.createSheet(" 赠送信息 ", 0);
        String headerArr[] = {"客户编号", "客户姓名", "客户电话", "地址", "代理商", "赠送商品", "赠送总数",
                "客户购买总数"};
        int h = 0;
        for (; h < headerArr.length; h++) {
            sheet1.addCell(new Label(h, 0, headerArr[h]));
        }
        int k = 1;
        for (Customer customer : giftInfo.keySet()) {
            Map<Goods4Agent, Integer> perGoodsInfo = giftInfo.get(customer);
            condition.clear();
            condition.put("customerId", customer.getCustomerId());
            ResultData fetchCustomerResponse = customerService.fetchCustomer(condition);
            CustomerVo customerDetail = ((List<CustomerVo>) fetchCustomerResponse.getData()).get(0);
            //以下是查找该客户最近一次的购买订单，顺便统计该客户的各商品购买盒数
            Map<String, Object[]> goodsMap = new HashMap<String, Object[]>();//商品ID->(商品name, 购买数量quantity)
            String phone = customer.getPhone().getPhone();
            condition.clear();
            condition.put("customerId", customer.getCustomerId());
            List<Integer> statusList = new ArrayList<>();
            statusList.add(1);
            statusList.add(2);
            statusList.add(3);
            statusList.add(4);
            statusList.add(5);
            statusList.add(6);
            condition.put("statusList", statusList);
            condition.put("blockFlag", false);
            List<SortRule> orderBy = new ArrayList<>();
            orderBy.add(new SortRule("create_time", "desc"));
            condition.put("sort", orderBy);
            ResultData fetchOrderItemResponse = orderService.fetchOrderItem(condition);
            if (fetchOrderItemResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                //分商品统计购买数量
                for (OrderItem orderItem : (List<OrderItem>) fetchOrderItemResponse.getData()) {
                    if (goodsMap.containsKey(orderItem.getGoods().getGoodsId())) {
                        Object[] goodsInfo = goodsMap.get(orderItem.getGoods().getGoodsId());
                        if (orderItem.getOrder().getType() != OrderType.GIFT) {
                            goodsInfo[1] = (Integer) goodsInfo[1] + orderItem.getGoodsQuantity();
                        }
                    } else {
                        Object[] goodsInfo = new Object[2];
                        goodsInfo[0] = orderItem.getGoods().getName();
                        if (orderItem.getOrder().getType() != OrderType.GIFT) {
                            goodsInfo[1] = orderItem.getGoodsQuantity();
                        } else {
                            goodsInfo[1] = Integer.valueOf(0);
                        }
                        goodsMap.put(orderItem.getGoods().getGoodsId(), goodsInfo);
                    }
                }

            }
            condition.clear();
            condition.put("receiverPhone", phone);
            condition.put("agentId", customerDetail.getAgent().getAgentId());
            condition.put("blockFlag", false);
            condition.put("status", statusList);
            condition.put("sort", orderBy);
            ResultData fetchCustomerOrderResponse = orderService.fetchCustomerOrder(condition);
            if (fetchCustomerOrderResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                //分商品统计购买数量
                for (CustomerOrder customerOrder : (List<CustomerOrder>) fetchCustomerOrderResponse.getData()) {
                    if (goodsMap.containsKey(customerOrder.getGoods().getGoodsId())) {
                        Object[] goodsInfo = goodsMap.get(customerOrder.getGoods().getGoodsId());
                        goodsInfo[1] = (Integer) goodsInfo[1] + customerOrder.getQuantity();
                    } else {
                        Object[] goodsInfo = new Object[2];
                        goodsInfo[0] = customerOrder.getGoods().getName();
                        goodsInfo[1] = customerOrder.getQuantity();
                        goodsMap.put(customerOrder.getGoods().getGoodsId(), goodsInfo);
                    }
                }
            }

            for (String goodsId : goodsMap.keySet()) {
                Object[] goodsObject = goodsMap.get(goodsId);
                Goods4Agent goods4Agent = new Goods4Agent();
                goods4Agent.setGoodsId(goodsId);
                sheet1.addCell(new Label(0, k, customer.getCustomerId()));
                sheet1.addCell(new Label(1, k, customer.getName()));
                sheet1.addCell(new Label(2, k, customer.getPhone().getPhone()));
                sheet1.addCell(new Label(3, k, customer.getAddress().getAddress()));
                sheet1.addCell(new Label(4, k, customerDetail.getAgent().getName()));
                sheet1.addCell(new Label(5, k, (String) goodsObject[0]));
                sheet1.addCell(new Label(6, k, perGoodsInfo.containsKey(goods4Agent) ? perGoodsInfo.get(goods4Agent).toString() : "0"));
                sheet1.addCell(new Label(7, k, ((Integer) ((goodsMap.get(goodsId))[1])).toString()));
                k++;
            }

        }

        book.write();
        book.close();
        os.flush();
        os.close();
        return null;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/downloadPayedBillExcel")
    public String downloadPayedBillExcel(HttpServletRequest request, HttpServletResponse response,String start, String end)
            throws IOException, RowsExceededException, WriteException {
    	 Map<String, Object> condition=new HashMap<>();
         if(!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)){
         	condition.put("start", start);
         	condition.put("end", end);
         }
    	 List<Integer> statusList=new ArrayList<>();
    	 statusList.add(1);
    	 statusList.add(2);
    	 condition.put("statusList", statusList);
    	 ResultData fetchResponse=statementService.payedBillStatement(condition);
    	 if (fetchResponse.getResponseCode()==ResponseCode.RESPONSE_OK) {
			
		 }
    	 return null;
    }
    
//    @RequestMapping(method = RequestMethod.GET, value = "/downloadRefundBillExcel")
//    public String downloadRefundBillExcel(HttpServletRequest request, HttpServletResponse response,String start, String end)
//            throws IOException, RowsExceededException, WriteException {
//    	 Map<String, Object> condition=new HashMap<>();
//         if(!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)){
//         	condition.put("start", start);
//         	condition.put("end", end);
//         }
//    	 ResultData fetchResponse=statementService.refundBillStatement(condition);
//    	 if (fetchResponse.getResponseCode()==ResponseCode.RESPONSE_OK) {
//
//		 }
//    	 return null;
//    }


}
