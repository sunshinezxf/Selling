package selling.sunshine.controller;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import selling.sunshine.form.GoodsForm;
import selling.sunshine.model.Admin;
import selling.sunshine.model.Agent;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.model.CustomerOrder;
import selling.sunshine.model.User;
import selling.sunshine.model.goods.Goods4Customer;
import selling.sunshine.model.goods.Thumbnail;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.CommodityService;
import selling.sunshine.service.LogService;
import selling.sunshine.service.OrderService;
import selling.sunshine.service.ToolService;
import selling.sunshine.service.UploadService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import selling.sunshine.utils.WechatConfig;
import selling.sunshine.utils.WechatUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
@RequestMapping("/commodity")
@RestController
public class CommodityController {

    private Logger logger = LoggerFactory.getLogger(CommodityController.class);

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UploadService uploadService;
    
    @Autowired
    private ToolService toolService;
    
    @Autowired
    private LogService logService;

    @RequestMapping(method = RequestMethod.GET, value = "/create")
    public ModelAndView create() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/goods/create");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ModelAndView create(@Valid GoodsForm form, BindingResult result,HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/commodity/create");
            return view;
        }

        Goods4Customer goods = new Goods4Customer(form.getName(), Double.parseDouble(form.getAgentPrice()), Double.parseDouble(form.getPrice()), form.getDescription());
        goods.setBlockFlag(form.isBlock());
        ResultData response = commodityService.createGoods4Customer(goods);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            if (user == null) {
            	view.setViewName("redirect:/commodity/create");
                return view;
            }
            Admin admin = user.getAdmin();
            BackOperationLog backOperationLog = new BackOperationLog(
                    admin.getUsername(), toolService.getIP(request) ,"管理员" + admin.getUsername() + "添加了一个新商品，商品名称:"
                    + form.getName() +"，商品ID：" + ((Goods4Customer)response.getData()).getGoodsId());
            logService.createbackOperationLog(backOperationLog);
            view.setViewName("redirect:/commodity/overview");
            return view;
        } else {
            view.setViewName("redirect:/commodity/create");
            return view;
        }
    }

	@RequestMapping(method = RequestMethod.GET, value = "/edit/{goodsId}")
	public ModelAndView edit(@PathVariable("goodsId") String goodsId) {
		ModelAndView view = new ModelAndView();
		Map<String, Object> condition = new HashMap<>();
		condition.put("goodsId", goodsId);
		ResultData resultData = commodityService.fetchGoods4Customer(condition);
		if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
			view.setViewName("redirect:/commodity/overview");
			return view;
		}
		Goods4Customer target = ((ArrayList<Goods4Customer>) resultData
				.getData()).get(0);
		view.addObject("goods", target);

		view.setViewName("/backend/goods/update");
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/thumbnails/{goodsId}")
	@ResponseBody
	public String thumbnails(@PathVariable("goodsId") String goodsId) {

		Map<String, Object> condition = new HashMap<>();
		condition.put("goodsId", goodsId);
		ResultData resultData = commodityService.fetchThumbnail(condition);
		List<Thumbnail> thumbnails = (List<Thumbnail>) resultData.getData();

		JSONArray resultArray = new JSONArray();
		JSONArray initialPreviewArray = new JSONArray();
		JSONArray initialPreviewConfigArray = new JSONArray();
		if (thumbnails.size() == 0) {

			resultArray.add(initialPreviewArray);
			resultArray.add(initialPreviewConfigArray);
			return resultArray.toJSONString();
		}
		for (Thumbnail thumbnail : thumbnails) {
			JSONObject initialPreviewConfigObject = new JSONObject();
			//initialPreviewArray.add("/selling" + thumbnail.getPath());
			initialPreviewArray.add(thumbnail.getPath());
//			initialPreviewConfigObject.put(
//					"url",
//					"/selling/commodity/delete/Thumbnail/"
//							+ thumbnail.getThumbnailId());
			initialPreviewConfigObject.put(
					"url",
					"/commodity/delete/Thumbnail/"
							+ thumbnail.getThumbnailId());
			initialPreviewConfigObject.put("key", thumbnail.getThumbnailId());
			initialPreviewConfigArray.add(initialPreviewConfigObject);
		}
		resultArray.add(initialPreviewArray);
		resultArray.add(initialPreviewConfigArray);
		return resultArray.toJSONString();

	}

	@RequestMapping(method = RequestMethod.POST, value = "/edit/{goodsId}")
	public ModelAndView edit(@PathVariable("goodsId") String goodsId,
			@Valid GoodsForm form, BindingResult result,HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		if (result.hasErrors()) {
			view.setViewName("redirect:/commodity/overview");
			return view;
		}
		Goods4Customer goods = new Goods4Customer(form.getName(),
				Double.parseDouble(form.getAgentPrice()),
				Double.parseDouble(form.getPrice()), form.getDescription());
		goods.setBlockFlag(form.isBlock());
		goods.setGoodsId(goodsId);
		ResultData response = commodityService.updateGoods4Customer(goods);

		List<Thumbnail> thumbnails = (List<Thumbnail>) commodityService
				.fetchThumbnail().getData();
		for (Thumbnail thumbnail : thumbnails) {
			thumbnail.setGoods((Goods4Customer) response.getData());
		}
		commodityService.updateThumbnails(thumbnails);

		view.setViewName("redirect:/commodity/overview");
		return view;

	}


    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/goods/overview");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public DataTablePage<Goods4Customer> overview(DataTableParam param) {
        DataTablePage<Goods4Customer> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        ResultData response = commodityService.fetchGoods4Customer(condition, param);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Goods4Customer>) response.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{goodsId}")
    public ModelAndView view(HttpServletRequest request, @PathVariable("goodsId") String goodsId, String agentId, String code, String state) {
        ModelAndView view = new ModelAndView();
        String openId = null;
        logger.debug(JSON.toJSONString(request.getSession().getAttribute("openId")));
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state)) {
        	HttpSession session = request.getSession();
        	if(session.getAttribute("openId") == null || session.getAttribute("openId").equals("")){
        		WechatConfig.oauthWechat(view, "/customer/component/goods_error_msg");
                view.setViewName("/customer/component/goods_error_msg");
                return view;
        	}
        }
        openId = WechatUtil.queryOauthOpenId(code);
        if(openId == null || openId.equals("")){
	        HttpSession session = request.getSession();
	        if(session.getAttribute("openId") != null && !session.getAttribute("openId").equals("")){
	        	openId = (String) session.getAttribute("openId");
	        }
        }
        view.addObject("wechat", openId);
        if (openId == null || openId.equals("")) {
        	WechatConfig.oauthWechat(view, "/customer/component/goods_error_msg");
            view.setViewName("/customer/component/goods_error_msg");
            return view;
        }
        if(openId != null && !openId.equals("")){
        	HttpSession session = request.getSession();
        	session.setAttribute("openId", openId);
        }
        logger.debug("oppppo" + openId);
        
        Map<String, Object> condition = new HashMap<>();
        condition.put("goodsId", goodsId);
        condition.put("blockFlag", false);
        ResultData fetchCommodityData = commodityService.fetchGoods4Customer(condition);
        if (fetchCommodityData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            //商品不存在错误页面!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        	WechatConfig.oauthWechat(view, "/customer/component/goods_error_msg");
            view.setViewName("/customer/component/goods_error_msg");
            return view;
        }
        Goods4Customer goods = ((List<Goods4Customer>) fetchCommodityData.getData()).get(0);
        if (!StringUtils.isEmpty(agentId)) {
            condition.clear();
            condition.put("agentId", agentId);
            condition.put("granted", true);
            condition.put("blockFlag", false);
            ResultData fetchAgentData = agentService.fetchAgent(condition);
            if (fetchAgentData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                //代理商不存在错误页面!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            	WechatConfig.oauthWechat(view, "/customer/component/agent_error_msg");
                view.setViewName("/customer/component/agent_error_msg");
                return view;
            }
            Agent agent = ((List<Agent>) fetchAgentData.getData()).get(0);
            view.addObject("agent", agent);
        }
        view.addObject("goods", goods);
        WechatConfig.oauthWechat(view, "/customer/goods/detail");
        view.setViewName("/customer/goods/detail");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/customerorder")
    public ModelAndView customerOrder(String orderId) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(orderId)) {
            condition.put("orderId", orderId);
        }
        if (condition.isEmpty()) {
            //订单不存在错误页面
        	WechatConfig.oauthWechat(view, "/customer/component/order_error_msg");
            view.setViewName("/customer/component/order_error_msg");
            return view;
        }
        ResultData fetchCustomerOrderData = orderService.fetchCustomerOrder(condition);
        if (fetchCustomerOrderData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            //订单不存在错误页面
        	WechatConfig.oauthWechat(view, "/customer/component/order_error_msg");
            view.setViewName("/customer/component/order_error_msg");
            return view;
        }
        CustomerOrder customerOrder = ((List<CustomerOrder>) fetchCustomerOrderData.getData()).get(0);
        view.addObject("customerOrder", customerOrder);
        WechatConfig.oauthWechat(view, "/customer/order/detail");
        view.setViewName("/customer/order/detail");
        return view;
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/detail/{goodsId}")
    public ResultData detail(@PathVariable("goodsId") String goodsId){
    	ResultData resultData=new ResultData();
    	Map<String, Object> dataMap = new HashMap<>();
    	
    	Map<String, Object> condition=new HashMap<>();
    	condition.put("goodsId", goodsId);
    	ResultData  queryData=commodityService.fetchGoods4Customer(condition);
    	if (queryData.getData()!=null) {
    		Goods4Customer goods=((List<Goods4Customer>)queryData.getData()).get(0);
    		dataMap.put("goods", goods);
    		List<Thumbnail> thumbnails=(List<Thumbnail>)commodityService.fetchThumbnail(condition).getData();
    		if (thumbnails.size() != 0) {
    			dataMap.put("thumbnails", thumbnails);
			}else {
				dataMap.put("thumbnails", 0);
			}
		}
        resultData.setData(dataMap);
        return resultData;   	
    }

    @RequestMapping(method = RequestMethod.GET, value = "/forbid/{goodsId}")
    public ModelAndView forbid(@PathVariable("goodsId") String goodsId,HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("goodsId", goodsId);
        ResultData resultData = commodityService.fetchGoods4Customer(condition);
        if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/commodity/overview");
            return view;
        }
        Goods4Customer target = ((List<Goods4Customer>) resultData.getData()).get(0);
        target.setBlockFlag(true);
        ResultData response = commodityService.updateGoods4Customer(target);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            if (user == null) {
            	view.setViewName("redirect:/commodity/create");
                return view;
            }
            Admin admin = user.getAdmin();
            BackOperationLog backOperationLog = new BackOperationLog(
                    admin.getUsername(), toolService.getIP(request) ,"管理员" + admin.getUsername() + "将商品"
                    + target.getName()+"下架，商品ID为："+target.getGoodsId());
            logService.createbackOperationLog(backOperationLog);
            view.setViewName("redirect:/commodity/overview");
        } else {
            view.setViewName("redirect:/commodity/overview");
        }
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/enable/{goodsId}")
    public ModelAndView enable(@PathVariable("goodsId") String goodsId,HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        condition.put("goodsId", goodsId);
        ResultData resultData = commodityService.fetchGoods4Customer(condition);
        if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/commodity/overview");
            return view;
        }
        Goods4Customer target = ((List<Goods4Customer>) resultData.getData()).get(0);
        target.setBlockFlag(false);
        ResultData response = commodityService.updateGoods4Customer(target);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            if (user == null) {
            	view.setViewName("redirect:/commodity/create");
                return view;
            }
            Admin admin = user.getAdmin();
            BackOperationLog backOperationLog = new BackOperationLog(
                    admin.getUsername(), toolService.getIP(request) ,"管理员" + admin.getUsername() + "将商品"
                    + target.getName()+"上架，商品ID为："+target.getGoodsId());
            logService.createbackOperationLog(backOperationLog);
            view.setViewName("redirect:/commodity/overview");
        } else {
            view.setViewName("redirect:/commodity/overview");
        }
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public String upload(MultipartHttpServletRequest request) {
    	System.err.println("test");
        String context = request.getSession().getServletContext().getRealPath("/");
        JSONObject resultObject = new JSONObject();
        try {
            String filename = "thumbnail";
            MultipartFile file = request.getFile(filename);
            if (file != null) {
                ResultData response = uploadService.upload(file, context);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    Thumbnail thumbnail = new Thumbnail((String) response.getData());
     

                    String thumbnailId = ((Thumbnail) commodityService.createThumbnail(thumbnail).getData()).getThumbnailId();
                    JSONArray initialPreviewArray = new JSONArray();
                    JSONArray initialPreviewConfigArray = new JSONArray();
                    JSONObject initialPreviewConfigObject = new JSONObject();
//                  initialPreviewArray.add("/selling" + response.getData().toString());
                    initialPreviewArray.add(response.getData().toString());
//                  initialPreviewConfigObject.put("url", "/selling/commodity/delete/Thumbnail/"+thumbnailId);
                    initialPreviewConfigObject.put("url", "/commodity/delete/Thumbnail/"+thumbnailId);
                    initialPreviewConfigObject.put("key", thumbnailId);
                    initialPreviewConfigArray.add(initialPreviewConfigObject);
                    resultObject.put("initialPreview", initialPreviewArray);
                    resultObject.put("initialPreviewConfig", initialPreviewConfigArray);
                    return resultObject.toJSONString();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        resultObject.put("error", "上传此图片发生错误，请重试！");
        return resultObject.toJSONString();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete/Thumbnail/{thumbnailId}")
    public String deleteThumbnail(@PathVariable("thumbnailId") String thumbnailId) {

        commodityService.deleteGoodsThumbnail(thumbnailId);
     
        JSONObject resultObject = new JSONObject();
        JSONArray initialPreviewArray = new JSONArray();
        JSONArray initialPreviewConfigArray = new JSONArray();
        resultObject.put("initialPreview", initialPreviewArray);
        resultObject.put("initialPreviewConfig", initialPreviewConfigArray);
        
        return resultObject.toJSONString();
    }


}
