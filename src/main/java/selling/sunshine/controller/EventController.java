package selling.sunshine.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.PUT;

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
import common.sunshine.model.selling.event.PromotionEvent;
import common.sunshine.model.selling.event.QuestionOption;
import common.sunshine.model.selling.event.support.ApplicationStatus;
import common.sunshine.model.selling.event.support.ChoiceType;
import common.sunshine.model.selling.event.support.EventType;
import common.sunshine.model.selling.event.support.PromotionConfig;
import common.sunshine.model.selling.goods.AbstractGoods;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.form.EventQuestionForm;
import selling.sunshine.form.GiftEventForm;
import selling.sunshine.form.PromotionConfigForm;
import selling.sunshine.form.PromotionEventForm;
import selling.sunshine.service.CommodityService;
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
	
	@Autowired
	private CommodityService commodityService;

	@RequestMapping(method = RequestMethod.GET, value = "/create/{type}")
	public ModelAndView create(@PathVariable("type") String type) {
		ModelAndView view = new ModelAndView();
		if (type.equals("gift")) {
			view.setViewName("backend/event/gift_create");
		}else {
			Map<String, Object> condition = new HashMap<>();
			condition.put("blockFlag",false);
	        ResultData response = commodityService.fetchGoods4Customer(condition);
	        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
	           view.addObject("goodsList", (List<Goods4Customer>)response.getData());
	        }
			view.setViewName("backend/event/promotion_create");
		}		
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/gift/create")
	public ResultData create(@RequestBody GiftEventForm form, HttpSession session) {
		ResultData resultData = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		ResultData queryResult = eventService.fetchEvent(condition);
		if (queryResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
			resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
			return resultData;
		}
		GiftEvent giftEvent = new GiftEvent();
		giftEvent.setTitle(form.getGiftEventTitle());
		giftEvent.setNickname(form.getGiftEventNickname());
		giftEvent.setStart(Timestamp.valueOf(form.getStartTime() + ":00"));
		giftEvent.setEnd(Timestamp.valueOf(form.getEndTime() + ":00"));
		giftEvent.setType(EventType.GIFT);
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
		resultData=eventService.createGiftEvent(giftEvent);
		return resultData;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/promotion/create")
	public ResultData create(@RequestBody PromotionEventForm form, HttpSession session) {
		ResultData resultData = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("blockFlag", false);
		ResultData queryResult = eventService.fetchEvent(condition);
		if (queryResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
			resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
			return resultData;
		}
		PromotionEvent event=new PromotionEvent();
		event.setTitle(form.getPromotionEventTitle());
		event.setNickname(form.getPromotionEventNickname());
		event.setStart(Timestamp.valueOf(form.getStartTime()));
		event.setEnd(Timestamp.valueOf(form.getEndTime()));
		event.setType(EventType.PROMOTION);
		PromotionConfigForm[] promotionConfigList=form.getPromotionConfigList();
		List<PromotionConfig> promotionConfigs=new ArrayList<>();
		for (PromotionConfigForm promotionConfigForm:promotionConfigList) {
			PromotionConfig config=new PromotionConfig();
			config.setCriterion(promotionConfigForm.getCriterion());
			config.setFull(promotionConfigForm.getFull());
			config.setGive(promotionConfigForm.getGive());
			Goods4Customer buyGoods=new Goods4Customer();
			buyGoods.setGoodsId(promotionConfigForm.getBuyGoods());
			Goods4Customer giveGoods=new Goods4Customer();
			giveGoods.setGoodsId(promotionConfigForm.getGiveGoods());
			config.setBuyGoods(buyGoods);
			config.setGiveGoods(giveGoods);
			promotionConfigs.add(config);
		}
		event.setConfig(promotionConfigs);
		resultData=eventService.createPromotionEvent(event);
		return resultData;
	}
	
    @RequestMapping(method = RequestMethod.GET, value = "/promotionConfig/{eventId}/{goodsId}")
    public ResultData promotionConfig(@PathVariable("eventId") String eventId,@PathVariable("goodsId") String goodsId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("buyGoodsId", goodsId);
        condition.put("eventId", eventId);
        condition.put("blockFlag", false);
        ResultData response = eventService.fetchPromotionConfig(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        }
        return result;
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/promotionConfig/{eventId}")
    public ResultData promotionConfig(@RequestBody PromotionConfigForm form,@PathVariable("eventId") String eventId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("buyGoodsId", form.getBuyGoods());
        condition.put("eventId", eventId);
        condition.put("blockFlag", false);
        ResultData response = eventService.fetchPromotionConfig(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
        	PromotionConfig config=((List<PromotionConfig>)response.getData()).get(0);
        	config.setCriterion(form.getCriterion());
        	config.setFull(form.getFull());
        	config.setGive(form.getGive());
			Goods4Customer giveGoods=new Goods4Customer();
			giveGoods.setGoodsId(form.getGiveGoods());
        	config.setGiveGoods(giveGoods);
        	result=eventService.createPromotionConfig(config);
        }
        return result;
    }

	@RequestMapping(method = RequestMethod.POST, value = "/overview")
	public DataTablePage<Event> overview(DataTableParam param) {
		DataTablePage<Event> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		ResultData fetchResponse = eventService.fetchEvent(condition, param);
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
			view.setViewName("backend/event/gift_preview");
		}else if(eventId.startsWith("PRE")){
			ResultData fetchResponse = eventService.fetchPromotionEvent(condition);
			if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
				PromotionEvent promotionEvent = ((List<PromotionEvent>) fetchResponse.getData()).get(0);
				view.addObject("promotionEvent", promotionEvent);
				condition.clear();
				condition.put("blockFlag",false);
		        ResultData response = commodityService.fetchGoods4Customer(condition);
		        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
		           view.addObject("goodsList", (List<Goods4Customer>)response.getData());
		        }
			}
			view.setViewName("backend/event/promotion_preview");
		}

		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/application/{eventId}")
	public ModelAndView application(@PathVariable("eventId") String eventId) {
		ModelAndView view = new ModelAndView();
		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
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
	public DataTablePage<EventApplication> application(@PathVariable("eventId") String eventId,DataTableParam param) {
		DataTablePage<EventApplication> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
		ResultData fetchResponse = eventService.fetchEventApplicationByPage(condition, param);
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<EventApplication>) fetchResponse.getData();
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/present/{eventId}")
	public ModelAndView present(@PathVariable("eventId") String eventId) {
		ModelAndView view = new ModelAndView();
		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
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
	public DataTablePage<EventOrder> present(@PathVariable("eventId") String eventId,DataTableParam param) {
		DataTablePage<EventOrder> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
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
			eventOrder.setStatus(OrderItemStatus.PAYED);
			Goods4Customer goods = new Goods4Customer();
			goods.setGoodsId("COMyfxwez26");
			eventOrder.setGoods(goods);
			resultData = eventService.createEventOrder(eventOrder);
		}

		return resultData;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/presentAll/{eventId}")
	public ResultData presentAll(@PathVariable("eventId") String eventId){
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
				eventOrder.setStatus(OrderItemStatus.PAYED);
				Goods4Customer goods = new Goods4Customer();
				goods.setGoodsId("COMyfxwez26");
				eventOrder.setGoods(goods);
				resultData = eventService.createEventOrder(eventOrder);
			}
		}
		return resultData;
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

//	@RequestMapping(method = RequestMethod.POST, value = "/application/detail/{applicationId}")
//	public ResultData detail(@PathVariable("applicationId") String applicationId) {
//		ResultData resultData = new ResultData();
//		Map<String, Object> condition = new HashMap<>();
//		condition.put("applicationId", applicationId);
//		resultData = eventService.fetchEventApplication(condition);
//		EventApplication eventApplication=((List<EventApplication>)resultData.getData()).get(0);
//		if (eventApplication.getStatus()==ApplicationStatus.APPROVED||eventApplication.getStatus()==ApplicationStatus.REJECTED) {
//			resultData.setDescription("done");;
//		}
//		return resultData;
//	}

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
				descriptionContent.append(eventOrder.getQuantity()).append("盒").append("（活动赠品）");

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
			descriptionContent.append(eventOrder.getQuantity()).append("盒").append("（活动赠品）");

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
	
	@RequestMapping(method = RequestMethod.POST, value = "/sendMessage/{eventId}")
	public ResultData sendMessage(@PathVariable("eventId") String eventId){
		ResultData resultData=new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
		condition.put("status", ApplicationStatus.APPROVED.getCode());
		ResultData fetchResponse = eventService.fetchEventApplication(condition);
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			List<EventApplication> eventApplications=(List<EventApplication>)fetchResponse.getData();
			for(EventApplication eventApplication:eventApplications){
				messageService.send(eventApplication.getDonorPhone(), "悠悠寸草心，云草见真情。恭喜您获赠云草纲目超细三七粉。8号发货，快递5天。如有疑问，欢迎咨询健康大使。预祝您身体健康，阖家团圆！【云草纲目】");
			}
		}
		return resultData;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/sendMessageAll/{eventId}")
	public ResultData sendMessageAll(@PathVariable("eventId") String eventId){
		ResultData resultData=new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("eventId", eventId);
		condition.put("status", ApplicationStatus.APPROVED.getCode());
		ResultData fetchResponse = eventService.fetchEventApplication(condition);
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			List<EventApplication> eventApplications=(List<EventApplication>)fetchResponse.getData();
			Set<String> phone=new HashSet<>();
			for(EventApplication eventApplication:eventApplications){
				phone.add(eventApplication.getDonorPhone());
				phone.add(eventApplication.getDoneePhone());
			}
			
			messageService.send(phone, "您获赠并服用云草超细三七粉后，身体健康状况有否改善？关注微信号：ycgm2016，得到更多的服务。【云草纲目】");
		}
		return resultData;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/all")
	public ResultData all(){
		ResultData resultData=new ResultData();
		Map<String, Object> condition = new HashMap<>();
		ResultData fetchResponse = eventService.fetchGiftEvent(condition);
		if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			List<Event> events=(List<Event>)fetchResponse.getData();
			resultData.setData(events);
			return resultData;
		}
		resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
		return resultData;
	}

}
