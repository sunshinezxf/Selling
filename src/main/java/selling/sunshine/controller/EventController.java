package selling.sunshine.controller;

import common.sunshine.model.selling.event.*;
import common.sunshine.model.selling.event.support.*;
import common.sunshine.model.selling.express.Express4Agent;
import common.sunshine.model.selling.express.Express4Application;
import common.sunshine.model.selling.express.Express4Customer;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.EventQuestionForm;
import selling.sunshine.form.GiftEventForm;
import selling.sunshine.form.PromotionConfigForm;
import selling.sunshine.form.PromotionEventForm;
import selling.sunshine.service.*;
import selling.sunshine.utils.PlatformConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

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
    private CommodityService commodityService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private OrderService orderService;

    /**
     * 根据活动的类别不同跳转到不同的活动创建界面（目前有两种类型的活动）
     * @param type
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/create/{type}")
    public ModelAndView create(@PathVariable("type") String type) {
        ModelAndView view = new ModelAndView();
        if (type.equals("gift")) {
            view.setViewName("backend/event/gift_create");
        } else if(type.equals("promotion")){
            Map<String, Object> condition = new HashMap<>();
            condition.put("blockFlag", false);
            ResultData response = commodityService.fetchGoods4Customer(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                view.addObject("goodsList", (List<Goods4Customer>) response.getData());
                condition.clear();
                response = commodityService.fetchGoods4Customer(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    view.addObject("giveGoodsList", (List<Goods4Customer>) response.getData());
                }
            }
            view.setViewName("backend/event/promotion_create");
        }
        return view;
    }

    /**
     * 添加一个新的gift活动
     * @param form
     * @param session
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/gift/create")
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
        giftEvent.setStart(Timestamp.valueOf(form.getStartTime()));
        giftEvent.setEnd(Timestamp.valueOf(form.getEndTime()));
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
        resultData = eventService.createGiftEvent(giftEvent);
        return resultData;
    }

    /**
     * 添加一个新的promotion活动
     * @param form
     * @param session
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/promotion/create")
    public ResultData create(@RequestBody PromotionEventForm form, HttpSession session) {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData queryResult = eventService.fetchPromotionEvent(condition);
        if (queryResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return resultData;
        }
        PromotionEvent event = new PromotionEvent();
        event.setTitle(form.getPromotionEventTitle());
        event.setNickname(form.getPromotionEventNickname());
        event.setStart(Timestamp.valueOf(form.getStartTime()));
        event.setEnd(Timestamp.valueOf(form.getEndTime()));
        event.setType(EventType.PROMOTION);
        switch (form.getPromotionEventType()){
            case "0" : event.setPromotionEventType(PromotionEventType.ALL);break;
            case "1" : event.setPromotionEventType(PromotionEventType.MALE);
            case "2" : event.setPromotionEventType(PromotionEventType.FEMALE);
            default : break;
        }
        PromotionConfigForm[] promotionConfigList = form.getPromotionConfigList();
        List<PromotionConfig> promotionConfigs = new ArrayList<>();
        for (PromotionConfigForm promotionConfigForm : promotionConfigList) {
            PromotionConfig config = new PromotionConfig();
            config.setCriterion(promotionConfigForm.getCriterion());
            config.setFull(promotionConfigForm.getFull());
            config.setGive(promotionConfigForm.getGive());
            Goods4Customer buyGoods = new Goods4Customer();
            buyGoods.setGoodsId(promotionConfigForm.getBuyGoods());
            Goods4Customer giveGoods = new Goods4Customer();
            giveGoods.setGoodsId(promotionConfigForm.getGiveGoods());
            config.setBuyGoods(buyGoods);
            config.setGiveGoods(giveGoods);
            promotionConfigs.add(config);
        }
        event.setConfig(promotionConfigs);
        resultData = eventService.createPromotionEvent(event);
        return resultData;
    }

    /**
     * 根据eventID得到满赠活动（promotion event）的商品配置
     * @param eventId
     * @param goodsId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/promotion/{eventId}/config/{goodsId}")
    public ResultData promotionConfig(@PathVariable("eventId") String eventId, @PathVariable("goodsId") String goodsId) {
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

    /**
     * 根据eventID修改满赠活动（promotion event）的商品配置
     * @param form
     * @param eventId
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/promotionConfig/{eventId}")
    public ResultData promotionConfig(@RequestBody PromotionConfigForm form, @PathVariable("eventId") String eventId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("buyGoodsId", form.getBuyGoods());
        condition.put("eventId", eventId);
        condition.put("blockFlag", false);
        ResultData response = eventService.fetchPromotionConfig(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            PromotionConfig config = ((List<PromotionConfig>) response.getData()).get(0);
            config.setCriterion(form.getCriterion());
            config.setFull(form.getFull());
            config.setGive(form.getGive());
            Goods4Customer giveGoods = new Goods4Customer();
            giveGoods.setGoodsId(form.getGiveGoods());
            config.setGiveGoods(giveGoods);
            result = eventService.createPromotionConfig(config);
        }
        return result;
    }

    /**
     * 后台活动数据获取datatable
     * @param param
     * @return
     */
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

    /**
     * 跳转到活动列表页面
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("backend/event/overview");
        return view;
    }

    /**
     * 跳转到活动详情页面
     * @return
     */
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
        } else if (eventId.startsWith("PRE")) {
            ResultData fetchResponse = eventService.fetchPromotionEvent(condition);
            if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                PromotionEvent promotionEvent = ((List<PromotionEvent>) fetchResponse.getData()).get(0);
                view.addObject("promotionEvent", promotionEvent);
                condition.clear();
                //condition.put("blockFlag", false);
                ResultData response = commodityService.fetchGoods4Customer(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    view.addObject("goodsList", (List<Goods4Customer>) response.getData());
                }
            }
            view.setViewName("backend/event/promotion_preview");
        }

        return view;
    }

    /**
     * 跳转到gift event的活动申请列表页面
     * @param eventId
     * @return
     */
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
                int size = ((List<EventApplication>) fetchResponse.getData()).size();
                view.addObject("size", size);
            }
        }
        view.setViewName("backend/event/application");
        return view;
    }

    /**
     * 得到gift event的活动申请列表信息
     * @param eventId
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/application/{eventId}")
    public DataTablePage<EventApplication> application(@PathVariable("eventId") String eventId, DataTableParam param) {
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

    /**
     * 跳转到event的赠送订单页面
     * @param eventId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/present/{eventId}")
    public ModelAndView present(@PathVariable("eventId") String eventId) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        condition.put("eventId", eventId);
        ResultData queryResult = eventService.fetchEvent(condition);
        if (queryResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
            view.addObject("giftEvent", ((List<Event>) queryResult.getData()).get(0));
            condition.put("eventId", ((List<Event>) queryResult.getData()).get(0).getEventId());
            List<Integer> status = new ArrayList<>();
            status.add(OrderItemStatus.PAYED.getCode());
            condition.put("status", status);
            ResultData fetchResponse = eventService.fetchEventOrder(condition);
            if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                int size = ((List<EventOrder>) fetchResponse.getData()).size();
                view.addObject("size", size);
            }
            view.setViewName("backend/event/present");
        } else {
            view.setViewName("redirect:/event/" + eventId);
        }

        return view;
    }

    /**
     * 得到event的赠送订单信息
     * @param eventId
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/present/{eventId}")
    public DataTablePage<EventOrder> present(@PathVariable("eventId") String eventId, DataTableParam param) {
        DataTablePage<EventOrder> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("eventId", eventId);
        condition.put("blockFlag", 0);
        ResultData fetchResponse = eventService.fetchEventOrderByPage(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<EventOrder>) fetchResponse.getData();
        }
        return result;
    }

    /**
     * 同意gift event的某一条申请赠送
     * @param applicationId
     * @return
     */
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

    /**
     * 同意gift event的所有申请赠送
     * @param eventId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/presentAll/{eventId}")
    public ResultData presentAll(@PathVariable("eventId") String eventId) {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("status", 0);
        resultData = eventService.fetchEventApplication(condition);
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<EventApplication> eventApplications = (List<EventApplication>) resultData.getData();
            for (EventApplication eventApplication : eventApplications) {
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

    /**
     * 拒绝gift event的某一条申请赠送
     * @param applicationId
     * @return
     */
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

    /**
     * 生成所有event order的快递信息excel供下载
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/expressAll")
    public String expressAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>(Arrays.asList(OrderItemStatus.PAYED.getCode()));
        condition.put("status", status);
        resultData = eventService.fetchEventOrder(condition);
        if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<EventOrder> eventOrders = (List<EventOrder>) resultData.getData();
            String context = request.getSession().getServletContext().getRealPath("/");
            StringBuffer path = new StringBuffer(context);
            path.append(PlatformConfig.getValue("express_template"));
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
            Sheet sheet = workbook.getSheetAt(0);
            for (int row = 3, i = 0; i < eventOrders.size(); i++, row++) {
                EventOrder eventOrder = eventOrders.get(i);
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

    /**
     * 生成一个event order的快递单信息excel供下载
     * @param orderId
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/express/{orderId}")
    public String express(@PathVariable("orderId") String orderId, HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    /**
     * 得到所有的event
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResultData all() {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        ResultData fetchResponse = eventService.fetchEvent(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Event> events = (List<Event>) fetchResponse.getData();
            resultData.setData(events);
            return resultData;
        }
        resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
        return resultData;
    }

    /**
     * 查询得到当前正在进行的promotion event
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/promotion/current")
    public ResultData current() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = eventService.fetchPromotionEvent(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(((List<PromotionEvent>) response.getData()).get(0));
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前暂无正在进行的满赠活动");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    /**
     * 手动设置快递单号生成event order的快递单信息
     * @param eventId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/promotion/setExpressNumber/{eventId}")
    public ModelAndView setExpressNumber(@PathVariable("eventId") String eventId) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        ResultData fetchResponse = new ResultData();
        condition.put("eventId", eventId);
        List<Integer> status = new ArrayList<>(Arrays.asList(OrderItemStatus.PAYED.getCode()));
        condition.put("status", status);
        fetchResponse = eventService.fetchEventOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<EventOrder> orderList = (List<EventOrder>) fetchResponse.getData();
            for (EventOrder eventOrder : orderList) {
                condition.clear();
                if (eventOrder.getLinkId().startsWith("ODR")) {
                    condition.put("orderId", eventOrder.getLinkId());
                    condition.put("phone", eventOrder.getDoneePhone());
                    fetchResponse = orderService.fetchOrderItem(condition);
                    if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        OrderItem item = ((List<OrderItem>) fetchResponse.getData()).get(0);
                        condition.clear();
                        ;
                        condition.put("orderItemId", item.getOrderItemId());
                        fetchResponse = expressService.fetchExpress4Agent(condition);
                        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                            Express4Agent express4Agent = ((List<Express4Agent>) fetchResponse.getData()).get(0);
                            eventOrder.setStatus(OrderItemStatus.SHIPPED);//更新eventorder状态为已发货
                            eventService.updateEventOrder(eventOrder);
                            Express4Application express4Application = new Express4Application();
                            express4Application.setExpressNumber(express4Agent.getExpressNumber());
                            express4Application.setEventOrder(eventOrder);
                            express4Application.setDescription("赠品");
                            express4Application.setGoodsName(eventOrder.getGoods().getName());
                            express4Application.setGoodsQuantity(eventOrder.getQuantity());
                            express4Application.setLinkId(eventOrder.getOrderId());
                            express4Application.setReceiverAddress(eventOrder.getDoneeAddress());
                            express4Application.setReceiverName(eventOrder.getDoneeName());
                            express4Application.setReceiverPhone(eventOrder.getDoneePhone());
                            express4Application.setSenderAddress("东风东路36号建工大厦2707室");
                            express4Application.setSenderName("云草纲目");
                            express4Application.setSenderPhone("15368099560");
                            expressService.createExpress(express4Application);//创建赠送订单的快递单

                        }
                    }

                } else {
                    condition.put("orderId", eventOrder.getLinkId());
                    fetchResponse = expressService.fetchExpress4Customer(condition);
                    if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        Express4Customer express4Customer = ((List<Express4Customer>) fetchResponse.getData()).get(0);
                        eventOrder.setStatus(OrderItemStatus.SHIPPED);//更新eventorder状态为已发货
                        eventService.updateEventOrder(eventOrder);
                        Express4Application express4Application = new Express4Application();
                        express4Application.setExpressNumber(express4Customer.getExpressNumber());
                        express4Application.setEventOrder(eventOrder);
                        express4Application.setDescription("赠品");
                        express4Application.setGoodsName(eventOrder.getGoods().getName());
                        express4Application.setGoodsQuantity(eventOrder.getQuantity());
                        express4Application.setLinkId(eventOrder.getOrderId());
                        express4Application.setReceiverAddress(eventOrder.getDoneeAddress());
                        express4Application.setReceiverName(eventOrder.getDoneeName());
                        express4Application.setReceiverPhone(eventOrder.getDoneePhone());
                        express4Application.setSenderAddress("东风东路36号建工大厦2707室");
                        express4Application.setSenderName("云草纲目");
                        express4Application.setSenderPhone("15368099560");
                        expressService.createExpress(express4Application);//创建赠送订单的快递单

                    }
                }
            }
        }

        view.setViewName("redirect:/event/present/" + eventId);
        return view;
    }
}
