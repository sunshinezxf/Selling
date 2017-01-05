package selling.sunshine.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.agent.AgentKPI;
import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.customer.CustomerAddress;
import common.sunshine.model.selling.customer.CustomerPhone;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.model.selling.order.support.OrderType;
import common.sunshine.model.selling.user.User;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import common.sunshine.utils.SortRule;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.CustomerAddressForm;
import selling.sunshine.form.CustomerForm;
import selling.sunshine.form.PurchaseForm;
import selling.sunshine.service.*;
import selling.sunshine.utils.WechatConfig;
import selling.sunshine.vo.customer.CustomerPurchase;
import selling.sunshine.vo.customer.CustomerVo;
import selling.sunshine.vo.order.OrderItemSum;

import javax.validation.Valid;
import java.util.*;

/**
 * Customer涉及到三个model, Customer, CustomerPhone & CustomerAddress
 * 所有的查询以及更新操作返回的都是CustomerVo
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

    @Autowired
    private EventService eventService;

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/customer/overview");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/overview/{agentId}")
    public ModelAndView overview(@PathVariable String agentId) {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/customer/overview");
        view.addObject("agentId", agentId);
        return view;
    }

//    @ResponseBody
//    @RequestMapping(method = RequestMethod.POST, value = "/overview/{agentId}")
//    public DataTablePage<CustomerVo> overview(@PathVariable String agentId, DataTableParam param) {
//        DataTablePage<CustomerVo> result = new DataTablePage<>();
//        if (StringUtils.isEmpty(result)) {
//            return result;
//        }
//        Map<String, Object> condition = new HashMap<>();
//        Subject subject = SecurityUtils.getSubject();
//        User user = (User) subject.getPrincipal();
//        if (user == null) {
//            return result;
//        }
//        if (user.getAgent() != null) {
//            condition.put("agentId", user.getAgent().getAgentId());
//        }
//        if (user.getAdmin() != null && !StringUtils.isEmpty(agentId)) {
//            condition.put("agentId", agentId);
//        }
//        List<SortRule> rules = new ArrayList<>();
//        rules.add(new SortRule("agent_id", "asc"));
//        condition.put("sort", rules);
//        ResultData fetchResponse = customerService.fetchCustomer(condition, param);
//        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
//            result = (DataTablePage<CustomerVo>) fetchResponse.getData();
//        }
//        return result;
//    }

    @RequestMapping(method = RequestMethod.GET, value = "/detail/{customerId}")
    @ResponseBody
    public ModelAndView detail(@PathVariable("customerId") String customerId) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        condition.put("customerId", customerId);
        ResultData fetchResponse = customerService.fetchCustomer(condition);
        if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/customer/overview");
            return view;
        }
        CustomerVo customer = ((List<CustomerVo>) fetchResponse.getData()).get(0);
        view.addObject("customer", customer);
        condition.clear();
        if (customer.getAgent() != null) {
            condition.put("agentId", customer.getAgent().getAgentId());
            fetchResponse = agentService.fetchAgent(condition);
            if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                view.setViewName("redirect:/customer/overview");
                return view;
            }
            Agent agent = ((List<Agent>) fetchResponse.getData()).get(0);
            view.addObject("agent", agent);
        }
        condition.clear();
        condition.put("customerId", customerId);
        fetchResponse = customerService.fetchCustomerAddress(condition);
        if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/customer/overview");
            return view;
        }
        view.setViewName("/backend/customer/detail");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/orderItem/{customerId}")
    public DataTablePage<OrderItem> orderItem(DataTableParam param, @PathVariable("customerId") String customerId) {
        DataTablePage<OrderItem> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("customerId", customerId);
//        List<Integer> status = new ArrayList<>(Arrays.asList(OrderItemStatus.PAYED.getCode(), OrderItemStatus.SHIPPED.getCode(), OrderItemStatus.RECEIVED.getCode(), OrderItemStatus.EXCHANGED.getCode()));
//        condition.put("statusList", status);
        ResultData queryResponse = orderService.fetchOrderItem(condition, param);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<OrderItem>) queryResponse.getData();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/customerOrder/{customerId}")
    public DataTablePage<CustomerOrder> customerOrder(DataTableParam param, @PathVariable("customerId") String customerId) {
        DataTablePage<CustomerOrder> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("customerId", customerId);
        ResultData queryResponse = customerService.fetchCustomerPhone(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CustomerPhone> phoneList = (List<CustomerPhone>) queryResponse.getData();
            List<String> phoneNumbers = new ArrayList<>();
            for (CustomerPhone phone : phoneList) {
                phoneNumbers.add(phone.getPhone());
            }
            condition.put("blockFlag", false);
            condition.put("phoneList", phoneNumbers);
//            List<Integer> status = new ArrayList<>(Arrays.asList(OrderItemStatus.PAYED.getCode(), OrderItemStatus.SHIPPED.getCode(), OrderItemStatus.RECEIVED.getCode(), OrderItemStatus.EXCHANGED.getCode()));
//            condition.put("status", status);
            queryResponse = orderService.fetchCustomerOrder(condition, param);
            if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result = (DataTablePage<CustomerOrder>) queryResponse.getData();
            }
        }
        return result;
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
        if (resultData.getResponseCode() != ResponseCode.RESPONSE_NULL) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return resultData;
        }

        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        common.sunshine.model.selling.agent.lite.Agent agent = user.getAgent();
        Customer customer = new Customer(customerForm.getName(), customerForm.getAddress(), customerForm.getPhone(), agent);
        resultData = customerService.createCustomer(customer);
        //添加顾客，要对顾客对应的代理商的agentKPI表进行修改
        condition.clear();
        condition.put("agentId", agent.getAgentId());
        ResultData fetchResponse = agentKPIService.fetchAgentKPI(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            AgentKPI agentKPI = ((List<AgentKPI>) fetchResponse.getData()).get(0);
            agentKPI.setCustomerQuantity(agentKPI.getCustomerQuantity() + 1);
            agentKPIService.updateAgentKPI(agentKPI);
        }
        return resultData;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/modify")
    public ResultData updateCustomer(String customerId, @Valid CustomerForm customerForm, BindingResult result) {
        ResultData response = new ResultData();
        if (result.hasErrors()) {
            response.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return response;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", customerForm.getPhone());
        condition.put("blockFlag", false);
        condition.put("customerBlockFlag", false);
        ResultData fetchCustomerPhoneResponse = customerService.fetchCustomerPhone(condition);
        if (((List<CustomerPhone>) fetchCustomerPhoneResponse.getData()).size() != 0) {
            CustomerPhone target = ((List<CustomerPhone>) fetchCustomerPhoneResponse.getData()).get(0);
            if (!target.getCustomer().getCustomerId().equals(customerId)) {
                response.setResponseCode(ResponseCode.RESPONSE_ERROR);
                return response;
            }
        }
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        common.sunshine.model.selling.agent.lite.Agent agent = user.getAgent();
        Customer customer = new Customer(customerForm.getName(), customerForm.getAddress(), customerForm.getPhone(), agent);
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
        Map<String, Object> condition = new HashMap<>();
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("customerId", customerId);
        ResultData fetchCustomerResponse = customerService.fetchCustomer(condition);
        if (fetchCustomerResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            response.setResponseCode(fetchCustomerResponse.getResponseCode());
            response.setDescription(fetchCustomerResponse.getDescription());
            return response;
        }
        CustomerVo vo = ((List<CustomerVo>) fetchCustomerResponse.getData()).get(0);
        Customer customer = new Customer(vo.getName(), vo.getAddress(), vo.getPhone(), user.getAgent());
        customer.setCustomerId(vo.getCustomerId());
        ResultData updateResponse = customerService.deleteCustomer(customer);
        if (updateResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            response.setResponseCode(updateResponse.getResponseCode());
            response.setDescription(updateResponse.getDescription());
            return response;
        }
        //删除顾客，要对顾客对应的代理商的agentKPI表进行修改
        condition.clear();
        condition.put("agentId", user.getAgent().getAgentId());
        ResultData fetchResponse = agentKPIService.fetchAgentKPI(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            AgentKPI agentKPI = ((List<AgentKPI>) fetchResponse.getData()).get(0);
            agentKPI.setCustomerQuantity(agentKPI.getCustomerQuantity() - 1);
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
        if (fetchCustomerResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未找到该客户");
            return result;
        }
        //以下是查找该客户最近一次的购买订单，顺便统计该客户的各商品购买盒数
        OrderItemSum orderItemSum = null;
        Map<String, Object[]> goodsMap = new HashMap<String, Object[]>();//商品ID->(商品name, 购买数量quantity)
        CustomerVo customer = ((List<CustomerVo>) fetchCustomerResponse.getData()).get(0);
        String phone = customer.getPhone();
        condition.clear();
        condition.put("customerId", customer.getCustomerId());
        List<Integer> statusList = new ArrayList<>(Arrays.asList(OrderItemStatus.PAYED.getCode(), OrderItemStatus.SHIPPED.getCode(), OrderItemStatus.RECEIVED.getCode(), OrderItemStatus.EXCHANGED.getCode()));
        condition.put("statusList", statusList);
        condition.put("blockFlag", false);
        List<SortRule> orderBy = new ArrayList<>();
        orderBy.add(new SortRule("create_time", "desc"));
        condition.put("sort", orderBy);
        ResultData fetchOrderItemResponse = orderService.fetchOrderItem(condition);
        if (fetchOrderItemResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            //分商品统计购买数量
            for (OrderItem orderItem : (List<OrderItem>) fetchOrderItemResponse.getData()) {
                if (orderItem.getOrder().getType() != OrderType.GIFT) {
                    if (goodsMap.containsKey(orderItem.getGoods().getGoodsId())) {
                        Object[] goodsInfo = goodsMap.get(orderItem.getGoods().getGoodsId());
                        goodsInfo[1] = (Integer) goodsInfo[1] + orderItem.getGoodsQuantity();
                    } else {
                        Object[] goodsInfo = new Object[2];
                        goodsInfo[0] = orderItem.getGoods().getName();
                        goodsInfo[1] = orderItem.getGoodsQuantity();
                        goodsMap.put(orderItem.getGoods().getGoodsId(), goodsInfo);
                    }
                }
            }

            OrderItem orderItem = ((List<OrderItem>) fetchOrderItemResponse.getData()).get(0);
            condition.clear();
            condition.put("agentId", user.getAgent().getAgentId());
            condition.put("orderId", orderItem.getOrderItemId());
            condition.put("blockFlag", false);
            ResultData fetchOrderItemSumResponse = orderService.fetchOrderItemSum(condition);
            if (fetchOrderItemSumResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                orderItemSum = ((List<OrderItemSum>) fetchOrderItemSumResponse.getData()).get(0);
            }
        }
        condition.clear();
        condition.put("receiverPhone", phone);
        condition.put("agentId", user.getAgent().getAgentId());
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

            CustomerOrder customerOrder = ((List<CustomerOrder>) fetchCustomerOrderResponse.getData()).get(0);
            condition.clear();
            condition.put("agentId", user.getAgent().getAgentId());
            condition.put("orderId", customerOrder.getOrderId());
            condition.put("blockFlag", false);
            ResultData fetchOrderItemSumResponse = orderService.fetchOrderItemSum(condition);
            if (fetchOrderItemSumResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                OrderItemSum orderItemSumTmp = ((List<OrderItemSum>) fetchOrderItemSumResponse.getData()).get(0);
                if (orderItemSum == null || orderItemSumTmp.getCreateAt().after(orderItemSum.getCreateAt())) {
                    orderItemSum = orderItemSumTmp;
                }
            }
        }
        JSONObject infoObject = new JSONObject();
        infoObject.put("lastOrder", orderItemSum);
        JSONArray goodsArray = new JSONArray();
        for (String goodsId : goodsMap.keySet()) {
            Object[] tmp = goodsMap.get(goodsId);
            JSONObject goodsObject = new JSONObject();
            goodsObject.put("name", tmp[0]);
            goodsObject.put("quantity", tmp[1]);
            goodsArray.add(goodsObject);
        }
        infoObject.put("salesInfo", goodsArray);
        result.setData(infoObject);
        if (orderItemSum == null || goodsArray.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/allOrder/{customerId}")
    public ModelAndView fetchCustomerAllOrder(@PathVariable("customerId") String customerId) {
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
        if (fetchCustomerResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            WechatConfig.oauthWechat(view, "/agent/login");
            view.setViewName("/agent/login");
            return view;
        }

        CustomerVo customer = ((List<CustomerVo>) fetchCustomerResponse.getData()).get(0);
        String phone = customer.getPhone();
        condition.clear();
        condition.put("customerId", customer.getCustomerId());
        List<Integer> statusList = new ArrayList<>(Arrays.asList(OrderItemStatus.PAYED.getCode(), OrderItemStatus.SHIPPED.getCode(), OrderItemStatus.RECEIVED.getCode(), OrderItemStatus.EXCHANGED.getCode()));
        condition.put("statusList", statusList);
        condition.put("blockFlag", false);
        List<SortRule> orderBy = new ArrayList<>();
        orderBy.add(new SortRule("create_time", "desc"));
        condition.put("sort", orderBy);
        ResultData fetchOrderItemResponse = orderService.fetchOrderItem(condition);
        List<OrderItem> orderItemList = (List<OrderItem>) (fetchOrderItemResponse.getData());

        condition.clear();
        condition.put("receiverPhone", phone);
        condition.put("agentId", user.getAgent().getAgentId());
        condition.put("blockFlag", false);
        condition.put("status", statusList);
        condition.put("sort", orderBy);
        ResultData fetchCustomerOrderResponse = orderService.fetchCustomerOrder(condition);
        List<CustomerOrder> customerOrderList = (List<CustomerOrder>) (fetchCustomerOrderResponse.getData());

        List<OrderItemSum> orderItemSumList = new ArrayList<>();
        if (orderItemList != null) {
            for (OrderItem orderItem : orderItemList) {
                condition.clear();
                condition.put("agentId", user.getAgent().getAgentId());
                condition.put("orderId", orderItem.getOrderItemId());
                condition.put("blockFlag", false);
                ResultData fetchOrderItemSumResponse = orderService.fetchOrderItemSum(condition);
                orderItemSumList.add(((List<OrderItemSum>) (fetchOrderItemSumResponse.getData())).get(0));
            }
        }
        if (customerOrderList != null) {
            for (CustomerOrder customerOrder : customerOrderList) {
                condition.clear();
                condition.put("agentId", user.getAgent().getAgentId());
                condition.put("orderId", customerOrder.getOrderId());
                condition.put("blockFlag", false);
                ResultData fetchOrderItemSumResponse = orderService.fetchOrderItemSum(condition);
                orderItemSumList.add(((List<OrderItemSum>) (fetchOrderItemSumResponse.getData())).get(0));
            }
        }
        Collections.sort(orderItemSumList, new Comparator<OrderItemSum>() {
            @Override
            public int compare(OrderItemSum o1, OrderItemSum o2) {
                return o2.getCreateAt().compareTo(o1.getCreateAt());
            }
        });

        view.addObject("customerName", customer.getName());
        view.addObject("customerPhone", customer.getPhone());
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
        //判断该顾客是否已经存在与顾客列表中
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", phone);
        condition.put("blockFlag", false);
        ResultData response = customerService.fetchCustomer(condition);
        //若存在，则查出这个customer并记录到order中
        Agent agent = null;
        if (!StringUtils.isEmpty(agentId)) {
            //验证该agentId是否存在
            condition.clear();
            condition.put("agentId", agentId);
            ResultData fetchAgentResponse = agentService.fetchAgent(condition);
            if (fetchAgentResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                agent = ((List<Agent>) fetchAgentResponse.getData()).get(0);
            }
        }
        CustomerVo vo = null;
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            vo = ((List<CustomerVo>) response.getData()).get(0);
        } else {
            Customer customer = new Customer(customerName, address, phone);
            if (!StringUtils.isEmpty(agent)) {
                customer.setAgent(new common.sunshine.model.selling.agent.lite.Agent(agent));
            }
            response = customerService.createCustomer(customer);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                vo = (CustomerVo) response.getData();
            } else {
                resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
                resultData.setDescription(response.getDescription());
                logger.error(response.getDescription());
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
        customerOrder.setCustomerId(vo.getCustomerId());
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
        status.add(OrderItemStatus.PAYED.getCode());
        status.add(OrderItemStatus.SHIPPED.getCode());
        status.add(OrderItemStatus.RECEIVED.getCode());
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
                    CustomerVo vo = ((List<CustomerVo>) customerService.fetchCustomer(condition).getData()).get(0);
                    Customer customer = new Customer(vo.getName(), vo.getAddress(), vo.getPhone(), vo.getAgent());
                    customer.setCustomerId(vo.getCustomerId());
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
        status.add(OrderItemStatus.PAYED.getCode());
        status.add(OrderItemStatus.SHIPPED.getCode());
        status.add(OrderItemStatus.RECEIVED.getCode());
        condition.put("status", status);
        fetchResponse = orderService.fetchCustomerOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CustomerOrder> list = (List<CustomerOrder>) fetchResponse.getData();
            if (!list.isEmpty()) {
                view.addObject("orderFromCustomer", list);
                empty = false;
            }
        }
        condition.clear();
        status.clear();
        condition.put("doneePhone", phone);
        condition.put("blockFlag", false);
        status.add(OrderItemStatus.PAYED.getCode());
        status.add(OrderItemStatus.SHIPPED.getCode());
        status.add(OrderItemStatus.RECEIVED.getCode());
        condition.put("status", status);
        fetchResponse = eventService.fetchEventOrder(condition);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<EventOrder> list = (List<EventOrder>) fetchResponse.getData();
            if (!list.isEmpty()) {
                view.addObject("orderFromEvent", list);
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


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/summary")
    public DataTablePage<CustomerPurchase> summary(DataTableParam param) {
        DataTablePage<CustomerPurchase> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            return result;
        }
        if (user.getAgent() != null) {
            condition.put("agentId", user.getAgent().getAgentId());
        }
        if (user.getAdmin() != null && !StringUtils.isEmpty(param.getParams())) {
            JSONObject json = JSON.parseObject(param.getParams());
            if (json.containsKey("agentId")) {
                condition.put("agentId", json.getString("agentId"));
            }
        }
        ResultData response = customerService.fetchCustomerPurchase(condition, param);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<CustomerPurchase>) response.getData();
        }
        return result;
    }
}
