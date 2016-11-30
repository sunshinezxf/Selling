package selling.sunshine.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.model.selling.order.CustomerOrder;
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
import selling.sunshine.service.CustomerService;
import selling.sunshine.service.OrderService;
import selling.sunshine.service.StatementService;
import selling.sunshine.vo.order.OrderItemSum;

@RequestMapping("/statement")
@RestController
public class StatementController {
	
    private Logger logger = LoggerFactory.getLogger(StatementController.class);
    
    @Autowired
    private StatementService statementService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET, value = "/downloadGiftExcel")
    public String downloadGiftExcel(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException, WriteException {
    	Map<String, Object> condition = new HashMap<String, Object>();
    	condition.put("blockFlag",false);
    	condition.put("type", 1);
    	ResultData fetchOrderResponse = orderService.fetchOrder(condition);
    	List<Order> orders = (List<Order>) fetchOrderResponse.getData();
    	Map<Customer, Map<Goods4Agent, Integer>> giftInfo = new HashMap<Customer, Map<Goods4Agent, Integer>>();
    	if(orders != null){
	    	for(Order order : orders){
	    		if(order.getOrderItems().isEmpty()){
	    			continue;
	    		}
	    		for(OrderItem orderItem : order.getOrderItems()){
	    			Customer customer = orderItem.getCustomer();
	    			Goods4Agent goods4Agent = orderItem.getGoods();
		    		if(giftInfo.containsKey(customer)){
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
        for(Customer customer : giftInfo.keySet()){
        	Map<Goods4Agent, Integer> perGoodsInfo = giftInfo.get(customer);
        	
        	condition.clear();
        	condition.put("customerId", customer.getCustomerId());
        	condition.put("blockFlag", false);
        	ResultData fetchCustomerResponse = customerService.fetchCustomer(condition);
        	Customer customerDetail = ((List<Customer>)fetchCustomerResponse.getData()).get(0);
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
        	if(fetchOrderItemResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
        		//分商品统计购买数量
        		for(OrderItem orderItem : (List<OrderItem>)fetchOrderItemResponse.getData()){
        			if(goodsMap.containsKey(orderItem.getGoods().getGoodsId())){
        				Object[] goodsInfo = goodsMap.get(orderItem.getGoods().getGoodsId());
        				if(orderItem.getOrder().getType() != OrderType.GIFT){
        					goodsInfo[1] = (Integer)goodsInfo[1] + orderItem.getGoodsQuantity();
        				}
        			} else {
        				Object[] goodsInfo = new Object[2];
        				goodsInfo[0] = orderItem.getGoods().getName();
        				if(orderItem.getOrder().getType() != OrderType.GIFT){
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
        	if(fetchCustomerOrderResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
        		//分商品统计购买数量
        		for(CustomerOrder customerOrder : (List<CustomerOrder>)fetchCustomerOrderResponse.getData()){
        			if(goodsMap.containsKey(customerOrder.getGoods().getGoodsId())){
        				Object[] goodsInfo = goodsMap.get(customerOrder.getGoods().getGoodsId());
        				goodsInfo[1] = (Integer)goodsInfo[1] + customerOrder.getQuantity();
        			} else {
        				Object[] goodsInfo = new Object[2];
        				goodsInfo[0] = customerOrder.getGoods().getName();
        				goodsInfo[1] = customerOrder.getQuantity();
        				goodsMap.put(customerOrder.getGoods().getGoodsId(), goodsInfo);
        			}
        		}
        	}
        	
        	for(String goodsId : goodsMap.keySet()){
        		Object[] goodsObject = goodsMap.get(goodsId);
        		Goods4Agent goods4Agent = new Goods4Agent();
        		goods4Agent.setGoodsId(goodsId);
        		sheet1.addCell(new Label(0, k, customer.getCustomerId()));
                sheet1.addCell(new Label(1, k, customer.getName()));
                sheet1.addCell(new Label(2, k, customer.getPhone().getPhone()));
                sheet1.addCell(new Label(3, k, customer.getAddress().getAddress()));
                sheet1.addCell(new Label(4, k, customerDetail.getAgent().getName()));
                sheet1.addCell(new Label(5, k, (String)goodsObject[0]));
                sheet1.addCell(new Label(6, k, perGoodsInfo.containsKey(goods4Agent) ? perGoodsInfo.get(goods4Agent).toString() : "0"));
                sheet1.addCell(new Label(7, k, ((Integer)((goodsMap.get(goodsId))[1])).toString()));
                k++;
        	}
        		
        }
        
        book.write();
        book.close();
        os.flush();
        os.close();
    	return null;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/downloadEventExcel")
    public String downloadEventExcel(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException, WriteException {
    	return null;
    }
}
