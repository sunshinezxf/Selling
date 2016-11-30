package selling.sunshine.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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

import common.sunshine.model.selling.customer.CustomerPhone;
import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.event.GiftEvent;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import selling.sunshine.service.CustomerService;
import selling.sunshine.service.EventService;
import selling.sunshine.service.OrderService;
import selling.sunshine.service.StatementService;

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

	@RequestMapping(method = RequestMethod.GET, value = "/downloadGiftExcel")
	public String downloadGiftExcel(HttpServletRequest request, HttpServletResponse response)
			throws IOException, RowsExceededException, WriteException {
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/downloadEventExcel")
	public String downloadEventExcel(HttpServletRequest request, HttpServletResponse response)
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
                  "被赠送商品名称", "被赠送商品数量","购买商品数量"};

		// 1.查询所有的event
		Map<String, Object> condition = new HashMap<>();
		ResultData queryData = new ResultData();
		queryData = eventService.fetchGiftEvent(condition);
		if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
			List<GiftEvent> events = (List<GiftEvent>) queryData.getData();
			// 2.对每一个event，统计被赠送人的个人信息、赠送信息和购买信息
			int count=0;
			for (GiftEvent event : events) {
				sheet = book.createSheet(event.getNickname() , count);
				count++;
				for (int h=0; h < headerArr.length; h++) {
	                sheet.addCell(new Label(h, 0, headerArr[h]));
	            }
				condition.clear();
				condition.put("eventId", event.getEventId());
				condition.put("status", 2);
				queryData = eventService.fetchEventApplication(condition);
				if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
					List<EventApplication> applications = (List<EventApplication>) queryData.getData();
					int i=0;
					for (EventApplication eventApplication:applications) {
						i++;
						condition.clear();
						condition.put("applicationId", eventApplication.getApplicationId());
						queryData=eventService.fetchEventOrder(condition);
						if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
							EventOrder eventOrder=((List<EventOrder>)queryData.getData()).get(0);						
							sheet.addCell(new Label(0, i, event.getNickname()));
							sheet.addCell(new Label(1, i, eventOrder.getDoneeName()));
							sheet.addCell(new Label(2, i, eventOrder.getDoneePhone()));
							sheet.addCell(new Label(3, i, eventOrder.getDoneeAddress()));
							sheet.addCell(new Label(4, i, eventApplication.getDonorName()));
							sheet.addCell(new Label(5, i, eventApplication.getDonorPhone()));
							sheet.addCell(new Label(6, i, eventOrder.getGoods().getName()));
							sheet.addCell(new Label(7, i, String.valueOf(eventOrder.getQuantity())));
							condition.clear();
							condition.put("phone",eventOrder.getDoneePhone());
							queryData=customerService.fetchCustomerPhone(condition);
							//在顾客电话表中有记录证明其有购买记录
							if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
								CustomerPhone customerPhone=((List<CustomerPhone>)queryData.getData()).get(0);
								//根据phone查customerorder表
								condition.clear();
								condition.put("receiverPhone", eventOrder.getDoneePhone());
								condition.put("goodsId", eventOrder.getGoods().getGoodsId());
								queryData=orderService.fetchCustomerOrder(condition);
								if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
								   
								}
								//根据customerid查orderitem表
								condition.clear();
								condition.put("customerId", customerPhone.getCustomer().getCustomerId());
								condition.put("goodsId", eventOrder.getGoods().getGoodsId());
								
							}else {
								//否则，没有购买，购买数量为0
								sheet.addCell(new Label(8, i+1, "0"));
							}
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
}
