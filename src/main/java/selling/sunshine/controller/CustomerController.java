package selling.sunshine.controller;

import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.customer.CustomerAddress;
import common.sunshine.model.selling.customer.CustomerPhone;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.user.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import selling.sunshine.form.CustomerAddressForm;
import selling.sunshine.form.CustomerForm;
import selling.sunshine.form.PurchaseForm;
import common.sunshine.utils.SortRule;
import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.agent.AgentKPI;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AgentKPIService;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.CommodityService;
import selling.sunshine.service.CustomerService;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.WechatConfig;
import selling.sunshine.vo.order.OrderItemSum;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by sunshine on 4/11/16.
 */
@RequestMapping("/customer")
@RestController
public class CustomerController {
    private Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommodityService commodityService;
    
    @Autowired
    private AgentKPIService agentKPIService;

    @RequestMapping(method = RequestMethod.GET, value = "/overview/{agentId}")
    public ModelAndView overview(@PathVariable String agentId) {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/customer/overview");
        view.addObject("agentId", agentId);
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/overview/{agentId}")
    public DataTablePage<Customer> overview(@PathVariable String agentId, DataTableParam param) {
        DataTablePage<Customer> result = new DataTablePage<Customer>();
        if (StringUtils.isEmpty(result)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", agentId);
        condition.put("blockFlag", false);
        ResultData fetchResponse = customerService.fetchCustomer(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<Customer>) fetchResponse.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/detail/{customerId}")
    @ResponseBody
    public ResultData detail(@PathVariable String customerId) {
        ResultData resultData = new ResultData();
        Map<String, Object> dataMap = new HashMap<>();

        Map<String, Object> condition = new HashMap<>();
        condition.put("customerId", customerId);
        Customer customer = ((List<Customer>) customerService.fetchCustomer(condition).getData()).get(0);
        Map<String, Object> agentCondition = new HashMap<>();
        agentCondition.put("agentId", customer.getAgent().getAgentId());
        Agent agent = ((List<Agent>) agentService.fetchAgent(agentCondition).getData()).get(0);
        Map<String, Object> orderItemCondition = new HashMap<>();
        orderItemCondition.put("customerId", customerId);
        List<OrderItem> orderItemList = (List<OrderItem>) orderService.fetchOrderItem(orderItemCondition).getData();
        List<CustomerAddress> addressList = (List<CustomerAddress>) customerService.fetchCustomerAddress(condition).getData();
        List<CustomerPhone> phoneList = (List<CustomerPhone>) customerService.fetchCustomerPhone(condition).getData();

        dataMap.put("customer", customer);
        dataMap.put("agent", agent);
        dataMap.put("orderItemList", orderItemList);
        dataMap.put("addressList", addressList);
        dataMap.put("phoneList", phoneList);
        resultData.setData(dataMap);
        return resultData;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public ResultData addCustomer(@Valid CustomerForm customerForm, BindingResult result) {
        ResultData resultData = new ResultData();
        if (result.hasErrors()) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return resultData;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", customerForm.getPhone());
        condition.put("blockFlag", false);
        condition.put("customerBlockFlag", false);
        resultData = customerService.fetchCustomerPhone(condition);
        if (((List<CustomerPhone>) resultData.getData()).size() != 0) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return resultData;
        }
     
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        common.sunshine.model.selling.agent.lite.Agent agent = user.getAgent();
        Customer customer = new Customer(customerForm.getName(),
                customerForm.getAddress(), customerForm.getPhone(), agent);
        resultData = customerService.createCustomer(customer);
        //添加顾客，要对顾客对应的代理商的agentKPI表进行修改
        condition.clear();
        condition.put("agentId", agent.getAgentId());
        ResultData fetchResponse=agentKPIService.fetchAgentKPI(condition);
        if (fetchResponse.getResponseCode()==ResponseCode.RESPONSE_OK) {
        	 AgentKPI agentKPI=((List<AgentKPI>)fetchResponse).get(0);
        	 agentKPI.setCustomerQuantity(agentKPI.getCustomerQuantity()+1);
        	 agentKPIService.updateAgentKPI(agentKPI);
		}
        return resultData;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/modify/{customerId}")
    public ResultData updateCustomer(@PathVariable("customerId") String customerId, @Valid CustomerForm customerForm, BindingResult result) {
        ResultData response = new ResultData();
        if (result.hasErrors()) {
            response.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return response;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", customerForm.getPhone());
        condition.put("blockFlag", false);
        condition.put("customerBlockFlag", false);
        response = customerService.fetchCustomerPhone(condition);
        if (((List<CustomerPhone>) response.getData()).size() != 0) {
            CustomerPhone target = ((List<CustomerPhone>) response.getData()).get(0);
            if (!target.getCustomer().getCustomerId().equals(customerId)) {
                response.setResponseCode(ResponseCode.RESPONSE_ERROR);
                return response;
            }
        }
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        common.sunshine.model.selling.agent.lite.Agent agent = user.getAgent();
        Customer customer = new Customer(customerForm.getName(),
                customerForm.getAddress(), customerForm.getPhone(), agent);
        customer.setCustomerId(customerId);
        ResultData updateResponse = customerService.updateCustomer(customer);
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            response.setData(updateResponse.getData());
        } else {
            response.setResponseCode(updateResponse.getResponseCode());
            response.setDescription(updateResponse.getDescription());
        }
        return response;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/modifyAddress/{customerId}")
    public ResultData updateCustomerAddress(@PathVariable("customerId") String customerId, @Valid CustomerAddressForm customerAddressForm, BindingResult result) {
        ResultData response = new ResultData();
        if (result.hasErrors()) {
            response.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return response;
        }
        Map<String, Object> condition = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        common.sunshine.model.selling.agent.lite.Agent agent = user.getAgent();
        Customer customer = new Customer(null, customerAddressForm.getAddress(), null, agent);
        customer.setCustomerId(customerId);
        ResultData updateResponse = customerService.updateCustomer(customer);
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            response.setData(updateResponse.getData());
        } else {
            response.setResponseCode(updateResponse.getResponseCode());
            response.setDescription(updateResponse.getDescription());
        }
        return response;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete/{customerId}")
    public ResultData deleteCustomer(@PathVariable String customerId) {
        ResultData response = new ResultData();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("customerId", customerId);
        ResultData fetchCustomerResponse = customerService.fetchCustomer(condition);
        if (fetchCustomerResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            response.setResponseCode(fetchCustomerResponse.getResponseCode());
            response.setDescription(fetchCustomerResponse.getDescription());
            return response;
        }
        Customer customer = ((List<Customer>) fetchCustomerResponse.getData()).get(0);
        ResultData updateResponse = customerService.deleteCustomer(customer);
        if (updateResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            response.setResponseCode(updateResponse.getResponseCode());
            response.setDescription(updateResponse.getDescription());
            return response;
        }
        //删除顾客，要对顾客对应的代理商的agentKPI表进行修改
        condition.clear();
        condition.put("agentId", user.getAgent().getAgentId());
        ResultData fetchResponse=agentKPIService.fetchAgentKPI(condition);
        if (fetchResponse.getResponseCode()==ResponseCode.RESPONSE_OK) {
        	 AgentKPI agentKPI=((List<AgentKPI>)fetchResponse).get(0);
        	 agentKPI.setCustomerQuantity(agentKPI.getCustomerQuantity()-1);
        	 agentKPIService.updateAgentKPI(agentKPI);
		}
        response.setData(updateResponse.getData());
        return response;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/{customerId}")
    public ResultData fetchCustomer(@PathVariable("customerId") String customerId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("customerId", customerId);
        condition.put("blockFlag", false);
        ResultData fetchResponse = customerService.fetchCustomer(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(((List<Customer>) fetchResponse.getData()).get(0));
        } else {
            fetchResponse.setResponseCode(fetchResponse.getResponseCode());
            fetchResponse.setDescription(fetchResponse.getDescription());
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/info/{customerId}")
    public ResultData fetchCustomerInfo(@PathVariable("customerId") String customerId) {
    	Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        ResultData result = new ResultData();
        if (user == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("您需要重新登录");
            return result;
        }
    	Map<String, Object> condition = new HashMap<String, Object>();
    	condition.put("customerId", customerId);
    	condition.put("blockFlag", false);
    	ResultData fetchCustomerResponse = customerService.fetchCustomer(condition);
    	if(fetchCustomerResponse.getResponseCode() != ResponseCode.RESPONSE_OK){
    		result.setResponseCode(ResponseCode.RESPONSE_ERROR);
    		result.setDescription("未找到该客户");
    		return result;
    	}
    	//以下是查找该客户最近一次的购买订单，顺便统计该客户的各商品购买盒数
    	OrderItemSum orderItemSum = null;
    	Map<String, Object[]> goodsMap = new HashMap<String, Object[]>();//商品ID->(商品name, 购买数量quantity)
    	Customer customer = ((List<Customer>)fetchCustomerResponse.getData()).get(0);
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
    				goodsInfo[1] = (Integer)goodsInfo[1] + orderItem.getGoodsQuantity();
    			} else {
    				Object[] goodsInfo = new Object[2];
    				goodsInfo[0] = orderItem.getGoods().getName();
    				goodsInfo[1] = orderItem.getGoodsQuantity();
    				goodsMap.put(orderItem.getGoods().getGoodsId(), goodsInfo);
    			}
    		}
    		
    		OrderItem orderItem = ((List<OrderItem>)fetchOrderItemResponse.getData()).get(0);
    		condition.clear();
    		condition.put("agentId", user.getAgent().getAgentId());
    		condition.put("orderId", orderItem.getOrderItemId());
    		condition.put("blockFlag", false);
    		ResultData fetchOrderItemSumResponse = orderService.fetchOrderItemSum(condition);
    		if(fetchOrderItemSumResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
    			orderItemSum = ((List<OrderItemSum>)fetchOrderItemSumResponse.getData()).get(0);
    		}
    	}
    	condition.clear();
    	condition.put("receiverPhone", phone);
    	condition.put("agentId", user.getAgent().getAgentId());
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
    		
    		CustomerOrder customerOrder = ((List<CustomerOrder>)fetchCustomerOrderResponse.getData()).get(0);
    		condition.clear();
    		condition.put("agentId", user.getAgent().getAgentId());
    		condition.put("orderId", customerOrder.getOrderId());
    		condition.put("blockFlag", false);
    		ResultData fetchOrderItemSumResponse = orderService.fetchOrderItemSum(condition);
    		if(fetchOrderItemSumResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
    			OrderItemSum orderItemSumTmp = ((List<OrderItemSum>)fetchOrderItemSumResponse.getData()).get(0);
    			if(orderItemSum == null || orderItemSumTmp.getCreateAt().after(orderItemSum.getCreateAt())){
    				orderItemSum = orderItemSumTmp;
    			}
    		}
    	}
    	JSONObject infoObject = new JSONObject();
    	infoObject.put("lastOrder", orderItemSum);
    	JSONArray goodsArray = new JSONArray();
    	for(String goodsId : goodsMap.keySet()){
    		Object[] tmp = goodsMap.get(goodsId);
    		JSONObject goodsObject = new JSONObject();
    		goodsObject.put("name", tmp[0]);
    		goodsObject.put("quantity", tmp[1]);
    		goodsArray.add(goodsObject);
    	}
    	infoObject.put("salesInfo", goodsArray);
    	result.setData(infoObject);
    	if(orderItemSum == null || goodsArray.isEmpty()){
    		result.setResponseCode(ResponseCode.RESPONSE_NULL);
    	}
    	return result;
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/allOrder/{customerId}")
    public ModelAndView fetchCustomerAllOrder(@PathVariable("customerId") String customerId){
    	ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            WechatConfig.oauthWechat(view, "/agent/login");
            view.setViewName("/agent/login");
            return view;
        }
    	Map<String, Object> condition = new HashMap<String, Object>();
    	condition.put("customerId", customerId);
    	condition.put("blockFlag", false);
    	ResultData fetchCustomerResponse = customerService.fetchCustomer(condition);
    	if(fetchCustomerResponse.getResponseCode() != ResponseCode.RESPONSE_OK){
    		 WechatConfig.oauthWechat(view, "/agent/login");
             view.setViewName("/agent/login");
             return view;
    	}
    	
    	Customer customer = ((List<Customer>)fetchCustomerResponse.getData()).get(0);
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
    	List<OrderItem> orderItemList = (List<OrderItem>)(fetchOrderItemResponse.getData()); 
    	
    	condition.clear();
    	condition.put("receiverPhone", phone);
    	condition.put("agentId", user.getAgent().getAgentId());
    	condition.put("blockFlag", false);
    	condition.put("status", statusList);
    	condition.put("sort", orderBy);
    	ResultData fetchCustomerOrderResponse = orderService.fetchCustomerOrder(condition);
    	List<CustomerOrder> customerOrderList = (List<CustomerOrder>)(fetchCustomerOrderResponse.getData());
    	
    	List<OrderItemSum> orderItemSumList = new ArrayList<OrderItemSum>();
    	if(orderItemList != null){
	    	for(OrderItem orderItem : orderItemList){
	    		condition.clear();
	    		condition.put("agentId", user.getAgent().getAgentId());
	    		condition.put("orderId", orderItem.getOrderItemId());
	    		condition.put("blockFlag", false);
	    		ResultData fetchOrderItemSumResponse = orderService.fetchOrderItemSum(condition);
	    		orderItemSumList.add(((List<OrderItemSum>)(fetchOrderItemSumResponse.getData())).get(0));
	    	}
    	}
    	if(customerOrderList != null){
	    	for(CustomerOrder customerOrder : customerOrderList){
	    		condition.clear();
	    		condition.put("agentId", user.getAgent().getAgentId());
	    		condition.put("orderId", customerOrder.getOrderId());
	    		condition.put("blockFlag", false);
	    		ResultData fetchOrderItemSumResponse = orderService.fetchOrderItemSum(condition);
	    		orderItemSumList.add(((List<OrderItemSum>)(fetchOrderItemSumResponse.getData())).get(0));
	    	}
    	}
    	Collections.sort(orderItemSumList, new Comparator<OrderItemSum>(){
			@Override
			public int compare(OrderItemSum o1, OrderItemSum o2) {
				return o2.getCreateAt().compareTo(o1.getCreateAt());
			}});
    	
    	view.addObject("customerName", customer.getName());
    	view.addObject("customerPhone", customer.getPhone().getPhone());
    	view.addObject("orderItemSums", orderItemSumList);
    	view.setViewName("/agent/customer/order_list");
    	return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/address/{customerId}")
    public ResultData fetchCustomerAddress(@PathVariable("customerId") String customerId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("customerId", customerId);
        List<SortRule> rule = new ArrayList<SortRule>();
        rule.add(new SortRule("create_time", "desc"));
        condition.put("sort", rule);
        ResultData fetchResponse = customerService.fetchCustomerAddress(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CustomerAddress> addressList = (List<CustomerAddress>) fetchResponse.getData();
            HashSet<CustomerAddress> addressSet = new HashSet<CustomerAddress>(addressList);
            addressList.clear();
            addressList.addAll(addressSet);
            int size = addressList.size() > 5 ? 5 : addressList.size();
            result.setData(addressList.subList(0, size));
        } else {
            result.setResponseCode(fetchResponse.getResponseCode());
            result.setDescription(fetchResponse.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/place")
    public ResultData placeOrder(@Valid PurchaseForm form, BindingResult result) {
        ResultData resultData = new ResultData();
        if (result.hasErrors()) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("表单错误");
            return resultData;
        }
        String goodsId = form.getGoodsId();
        String agentId = form.getAgentId();
        String customerName = form.getCustomerName();
        int goodsQuantity = Integer.parseInt(form.getGoodsNum());
        String phone = form.getPhone();
        String address = form.getAddress();
        String wechat = form.getWechat();
        Map<String, Object> condition = new HashMap<>();
        //判断代理商是否合法
        Agent agent = null;
        if (agentId != null && agentId != "") {
            condition.clear();
            condition.put("agentId", agentId);
            condition.put("granted", true);
            condition.put("blockFlag", false);
            ResultData fetchAgentData = agentService.fetchAgent(condition);
            if (fetchAgentData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                agent = ((List<Agent>) fetchAgentData.getData()).get(0);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        Map<String, Object> condition = new HashMap<>();
                        condition.put("phone", phone);
                        condition.put("blockFlag", false);
                        condition.put("customerBlockFlag", false);
                        ResultData response = customerService.fetchCustomerPhone(condition);
                        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                            common.sunshine.model.selling.agent.lite.Agent agent = new common.sunshine.model.selling.agent.lite.Agent();
                            agent.setAgentId(agentId);
                            Customer customer = new Customer(customerName, address, phone, agent);
                            customerService.createCustomer(customer);
                        }
                    }
                };
                thread.start();
            }
        }
        //判断商品是否合法
        condition.clear();
        condition.put("goodsId", goodsId);
        condition.put("blockFlag", false);
        ResultData fetchCommodityData = commodityService.fetchGoods4Customer(condition);
        if (fetchCommodityData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("没有找到商品");
            return resultData;
        }
        Goods4Customer goods = ((List<Goods4Customer>) fetchCommodityData.getData()).get(0);
        double goodsPrice = agent == null ? goods.getCustomerPrice() : goods.getAgentPrice();
        double totalPrice = goodsPrice * goodsQuantity;
        common.sunshine.model.selling.agent.lite.Agent agentLite = new common.sunshine.model.selling.agent.lite.Agent(agent);
        CustomerOrder customerOrder = new CustomerOrder(goods, goodsQuantity, customerName, phone, address, agentLite);
        customerOrder.setTotalPrice(totalPrice);
        customerOrder.setWechat(wechat);

        ResultData fetchResponse = orderService.placeOrder(customerOrder);
        if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("下单错误");
            return resultData;
        }
        resultData.setData(fetchResponse.getData());
        return resultData;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/consult")
    public ModelAndView consult() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/customer/order/consult");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/order/search")
    public ModelAndView search(String phone) {
        ModelAndView view = new ModelAndView();
        boolean empty = true;
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", phone);
        condition.put("blockFlag", false);
        List<Integer> status = new ArrayList<>();
        status.add(1);
        status.add(2);
        status.add(3);
        condition.put("statusList", status);
        ResultData fetchResponse = orderService.fetchOrderItem(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<OrderItem> list = (List<OrderItem>) fetchResponse.getData();
            if (!list.isEmpty()) {
                for (OrderItem item : list) {
                    String orderId = item.getOrder().getOrderId();
                    condition.clear();
                    condition.put("orderId", orderId);
                    Order order = ((List<Order>) orderService.fetchOrder(condition).getData()).get(0);
                    item.setOrder(order);
                    condition.clear();
                    condition.put("customerId", item.getCustomer().getCustomerId());
                    Customer customer = ((List<Customer>) customerService.fetchCustomer(condition).getData()).get(0);
                    item.setCustomer(customer);
                }
                view.addObject("orderFromAgent", list);
                empty = false;
            }
        }
        condition.clear();
        status.clear();
        condition.put("receiverPhone", phone);
        condition.put("blockFlag", false);
        status.add(1);
        status.add(2);
        status.add(3);
        condition.put("status", status);
        fetchResponse = orderService.fetchCustomerOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CustomerOrder> list = (List<CustomerOrder>) fetchResponse.getData();
            if (!list.isEmpty()) {
                view.addObject("orderFromCustomer", list);
                empty = false;
            }
        }
        if (empty) {
            view.setViewName("/customer/component/order_error_msg");
            return view;
        }
        view.setViewName("/customer/order/order_list");
        return view;
    }
}
