package selling.sunshine.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import common.sunshine.model.selling.event.Event;
import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.event.EventQuestion;
import common.sunshine.model.selling.event.GiftEvent;
import common.sunshine.model.selling.event.QuestionOption;
import common.sunshine.model.selling.event.support.ApplicationStatus;
import common.sunshine.model.selling.event.support.ChoiceType;

import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.form.EventQuestionForm;
import selling.sunshine.form.GiftEventForm;
import selling.sunshine.service.EventService;
import selling.sunshine.service.MessageService;
import selling.sunshine.utils.PlatformConfig;

/**
 * Created by sunshine on 8/23/16.
 */
@RestController
@RequestMapping("/event")
public class EventController {
	private Logger logger = LoggerFactory.getLogger(EventController.class);

	@Autowired
	private EventService eventService;
	
	@Autowired
	private MessageService messageService;

	@RequestMapping(method = RequestMethod.GET, value = "/create")
	public ModelAndView create() {
		ModelAndView view = new ModelAndView();
		view.setViewName("backend/event/create");
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/create")
	public ResultData create(@RequestBody GiftEventForm form, HttpSession session) {
		ResultData resultData = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		ResultData queryResult = eventService.fetchGiftEvent(condition);
		if (queryResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
			resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
			return resultData;
		}
		GiftEvent giftEvent = new GiftEvent();
		giftEvent.setTitle(form.getGiftEventTitle());
		giftEvent.setNickname(form.getGiftEventNickname());
		giftEvent.setStart(Timestamp.valueOf(form.getStartTime() + ":00"));
		giftEvent.setEnd(Timestamp.valueOf(form.getEndTime() + ":00"));
		List<EventQuestion> eventQuestions = new ArrayList<>();
		for (int i = 0; i < form.getQuestionList().length; i++) {
			EventQuestionForm eventQuestionForm = form.getQuestionList()[i];
			EventQuestion eventQuestion = new EventQuestion();
			eventQuestion.setContent(eventQuestionForm.getContent());
			eventQuestion.setRank(eventQuestionForm.getRank());
			if (eventQuestionForm.getType().equals("0")) {
				eventQuestion.setType(ChoiceType.EXCLUSIVE);
			} else {
				eventQuestion.setType(ChoiceType.MULTIPLE);
			}
			eventQuestion.setEvent(giftEvent);
			List<QuestionOption> questionOptions = new ArrayList<>();
			for (int j = 0; j < eventQuestionForm.getQuestionOptionList().length; j++) {
				QuestionOption questionOption = new QuestionOption();
				questionOption.setQuestion(eventQuestion);
				questionOption.setValue(eventQuestionForm.getQuestionOptionList()[j]);
				questionOptions.add(questionOption);
			}
			eventQuestion.setOptions(questionOptions);
			eventQuestions.add(eventQuestion);
		}
		giftEvent.setQuestions(eventQuestions);
		eventService.createGiftEvent(giftEvent);
		return resultData;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/overview")
	public DataTablePage<Event> overview(DataTableParam param) {
		DataTablePage<Event> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		ResultData fetchResponse = eventService.fetchGiftEventByPage(condition, param);
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<Event>) fetchResponse.getData();
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/overview")
	public ModelAndView overview() {
		ModelAndView view = new ModelAndView();
		view.setViewName("backend/event/overview");
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{eventId}")
	public ModelAndView preview(@PathVariable("eventId") String eventId) {
		ModelAndView view = new ModelAndView();

		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
		if (eventId.startsWith("GEV")) {
			ResultData fetchResponse = eventService.fetchGiftEvent(condition);
			if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
				GiftEvent giftEvent = ((List<GiftEvent>) fetchResponse.getData()).get(0);
				view.addObject("giftEvent", giftEvent);
			}
			view.setViewName("backend/event/preview");
		}

		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/application")
	public ModelAndView application() {
		ModelAndView view = new ModelAndView();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		ResultData queryResult = eventService.fetchGiftEvent(condition);
		if (queryResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
			view.addObject("giftEvent", ((List<GiftEvent>) queryResult.getData()).get(0));
			condition.clear();
			condition.put("status", 0);
			ResultData fetchResponse = eventService.fetchEventApplication(condition);
			if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
				int size=((List<EventApplication>)fetchResponse.getData()).size();
				view.addObject("size", size);
			}
		}
		view.setViewName("backend/event/application");
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/application/{eventId}")
	public DataTablePage<EventApplication> application(@PathVariable("eventId") String eventId, DataTableParam param) {
		DataTablePage<EventApplication> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
		condition.put("status", 0);
		ResultData fetchResponse = eventService.fetchEventApplicationByPage(condition, param);
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<EventApplication>) fetchResponse.getData();
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/present")
	public ModelAndView present() {
		ModelAndView view = new ModelAndView();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		ResultData queryResult = eventService.fetchGiftEvent(condition);
		if (queryResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
			view.addObject("giftEvent", ((List<GiftEvent>) queryResult.getData()).get(0));
			condition.put("eventId", ((List<GiftEvent>) queryResult.getData()).get(0).getEventId());
			List<Integer> status = new ArrayList<>();
			status.add(1);
			condition.put("status", status);
			ResultData fetchResponse = eventService.fetchEventOrder(condition);
			if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
				int size=((List<EventOrder>)fetchResponse.getData()).size();
				view.addObject("size", size);
			}
		}
		view.setViewName("backend/event/present");
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/present/{eventId}")
	public DataTablePage<EventOrder> present(@PathVariable("eventId") String eventId, DataTableParam param) {
		DataTablePage<EventOrder> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
		List<Integer> status = new ArrayList<>();
		status.add(1);
		condition.put("status", status);
		ResultData fetchResponse = eventService.fetchEventOrderByPage(condition, param);
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<EventOrder>) fetchResponse.getData();
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/agree/{applicationId}")
	public ResultData agree(@PathVariable("applicationId") String applicationId) {
		ResultData resultData = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("applicationId", applicationId);
		resultData = eventService.fetchEventApplication(condition);
		if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
			EventApplication eventApplication = (EventApplication) ((List<EventApplication>) resultData.getData())
					.get(0);
			eventApplication.setStatus(ApplicationStatus.APPROVED);
			resultData = eventService.updateEventApplication(eventApplication);
			EventOrder eventOrder = new EventOrder();
			eventOrder.setDoneeName(eventApplication.getDoneeName());
			eventOrder.setDoneePhone(eventApplication.getDoneePhone());
			eventOrder.setDoneeAddress(eventApplication.getDoneeAddress());
			eventOrder.setQuantity(1);
			eventOrder.setApplication(eventApplication);
			eventOrder.setEvent(eventApplication.getEvent());
			eventOrder.setOrderStatus(OrderItemStatus.PAYED);
			Goods4Customer goods = new Goods4Customer();
			goods.setGoodsId("COMyfxwez26");
			eventOrder.setGoods(goods);
			resultData = eventService.createEventOrder(eventOrder);
		}

		return resultData;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/presentAll")
	public ModelAndView presentAll(){
		ModelAndView view=new ModelAndView();
		ResultData resultData = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("status", 0);
		resultData = eventService.fetchEventApplication(condition);
		if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
			List<EventApplication> eventApplications=(List<EventApplication>) resultData.getData();
			for (EventApplication eventApplication:eventApplications) {
				eventApplication.setStatus(ApplicationStatus.APPROVED);
				resultData = eventService.updateEventApplication(eventApplication);
				EventOrder eventOrder = new EventOrder();
				eventOrder.setDoneeName(eventApplication.getDoneeName());
				eventOrder.setDoneePhone(eventApplication.getDoneePhone());
				eventOrder.setDoneeAddress(eventApplication.getDoneeAddress());
				eventOrder.setQuantity(1);
				eventOrder.setApplication(eventApplication);
				eventOrder.setEvent(eventApplication.getEvent());
				eventOrder.setOrderStatus(OrderItemStatus.PAYED);
				Goods4Customer goods = new Goods4Customer();
				goods.setGoodsId("COMyfxwez26");
				eventOrder.setGoods(goods);
				resultData = eventService.createEventOrder(eventOrder);
			}
		}
        view.setViewName("redirect:/event/present");
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/reject/{applicationId}")
	public ResultData reject(@PathVariable("applicationId") String applicationId) {
		ResultData resultData = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("applicationId", applicationId);
		resultData = eventService.fetchEventApplication(condition);
		if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
			EventApplication eventApplication = (EventApplication) ((List<EventApplication>) resultData.getData())
					.get(0);
			eventApplication.setStatus(ApplicationStatus.REJECTED);
			resultData = eventService.updateEventApplication(eventApplication);
		}

		return resultData;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/application/detail/{applicationId}")
	public ResultData detail(@PathVariable("applicationId") String applicationId) {
		ResultData resultData = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("applicationId", applicationId);
		resultData = eventService.fetchEventApplication(condition);
		return resultData;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/order/expressAll")
	public String expressAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ResultData resultData = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		List<Integer> status = new ArrayList<>();
		status.add(1);
		condition.put("status", status);
		resultData = eventService.fetchEventOrder(condition);
		if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
			List<EventOrder> eventOrders = (List<EventOrder>) resultData.getData();
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
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("快递单.xls", "utf-8"));
			OutputStream os = response.getOutputStream();

			NPOIFSFileSystem pkg = new NPOIFSFileSystem(file);
			Workbook workbook = new HSSFWorkbook(pkg.getRoot(), true);
			for (int row = 3, i = 0; i < eventOrders.size(); i++, row++) {
				EventOrder eventOrder = eventOrders.get(i);
				Sheet sheet = workbook.getSheetAt(0);
				Row current = sheet.createRow(row);
				Cell senderName = current.createCell(2);
				senderName.setCellValue(PlatformConfig.getValue("sender_name"));
				Cell senderPhone = current.createCell(3);
				senderPhone.setCellValue(PlatformConfig.getValue("sender_phone"));
				Cell senderAddress = current.createCell(7);
				senderAddress.setCellValue(PlatformConfig.getValue("sender_address"));
				Cell receiverName = current.createCell(8);
				receiverName.setCellValue(eventOrder.getDoneeName());
				Cell receiverPhone = current.createCell(9);
				receiverPhone.setCellValue(eventOrder.getDoneePhone());
				Cell receiverAddress = current.createCell(13);
				receiverAddress.setCellValue(eventOrder.getDoneeAddress());
				Cell goods = current.createCell(14);
				goods.setCellValue(eventOrder.getGoods().getName());
				Cell description = current.createCell(22);
				StringBuffer descriptionContent = new StringBuffer();
				descriptionContent.append(eventOrder.getQuantity()).append("盒");

				description.setCellValue(descriptionContent.toString());
				Cell orderNo = current.createCell(37);
				orderNo.setCellValue(eventOrder.getOrderId());

			}
			// 写入数据并关闭文件
			workbook.write(os);
			workbook.close();
			os.flush();
			os.close();
		}

		return "";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/order/express/{orderId}")
	public String express(@PathVariable("orderId") String orderId,HttpServletRequest request, HttpServletResponse response) throws IOException {
		ResultData resultData = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		List<Integer> status = new ArrayList<>();
		status.add(1);
		condition.put("status", status);
		condition.put("orderId", orderId);
		resultData = eventService.fetchEventOrder(condition);
		if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
			EventOrder eventOrder = ((List<EventOrder>) resultData.getData()).get(0);
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
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("快递单.xls", "utf-8"));
			OutputStream os = response.getOutputStream();

			NPOIFSFileSystem pkg = new NPOIFSFileSystem(file);
			Workbook workbook = new HSSFWorkbook(pkg.getRoot(), true);
			int row = 3;
			Sheet sheet = workbook.getSheetAt(0);
			Row current = sheet.createRow(row);
			Cell senderName = current.createCell(2);
			senderName.setCellValue(PlatformConfig.getValue("sender_name"));
			Cell senderPhone = current.createCell(3);
			senderPhone.setCellValue(PlatformConfig.getValue("sender_phone"));
			Cell senderAddress = current.createCell(7);
			senderAddress.setCellValue(PlatformConfig.getValue("sender_address"));
			Cell receiverName = current.createCell(8);
			receiverName.setCellValue(eventOrder.getDoneeName());
			Cell receiverPhone = current.createCell(9);
			receiverPhone.setCellValue(eventOrder.getDoneePhone());
			Cell receiverAddress = current.createCell(13);
			receiverAddress.setCellValue(eventOrder.getDoneeAddress());
			Cell goods = current.createCell(14);
			goods.setCellValue(eventOrder.getGoods().getName());
			Cell description = current.createCell(22);
			StringBuffer descriptionContent = new StringBuffer();
			descriptionContent.append(eventOrder.getQuantity()).append("盒");

			description.setCellValue(descriptionContent.toString());
			Cell orderNo = current.createCell(37);
			orderNo.setCellValue(eventOrder.getOrderId());

			// 写入数据并关闭文件
			workbook.write(os);
			workbook.close();
			os.flush();
			os.close();
		}
		return "";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/sendMessage/{eventId}")
	public ResultData sendMessage(@PathVariable("eventId") String eventId){
		ResultData resultData=new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
		condition.put("status", 2);
		ResultData fetchResponse = eventService.fetchEventApplication(condition);
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			List<EventApplication> eventApplications=(List<EventApplication>)fetchResponse.getData();
			for(EventApplication eventApplication:eventApplications){
				messageService.send(eventApplication.getDonorPhone(), "【云草纲目】悠悠寸草心，云草见真情。恭喜您获赠云草纲目超细三七粉。8号发货，快递5天。如有疑问，欢迎咨询健康大使。预祝您身体健康，阖家团圆！");
			}
		}
		return resultData;
	}

}
