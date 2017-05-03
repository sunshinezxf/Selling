package selling.sunshine.service.impl;

import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.agent.support.AgentType;
import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.event.Event;
import common.sunshine.model.selling.event.support.EventType;
import common.sunshine.model.selling.event.support.PromotionConfig;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.model.selling.order.support.OrderStatus;
import common.sunshine.model.selling.order.support.OrderType;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.pagination.MobilePageParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import selling.sunshine.dao.*;
import selling.sunshine.service.OrderService;
import selling.sunshine.vo.customer.CustomerVo;
import selling.sunshine.vo.order.OrderItemSum;

import java.sql.Timestamp;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderPoolDao orderPoolDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private CustomerOrderDao customerOrderDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private AgentDao agentDao;

    /**
     * 下单主方法
     */
    @Override
    public ResultData placeOrder(Order order) {
        ResultData result = new ResultData();
        ResultData insertResponse = orderDao.insertOrder(order);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    /**
     * 仅仅生成一个Order,不生成Order里的OrderItem
     */
    @Override
    public ResultData createOrder(Order order) {
        ResultData result = new ResultData();
        ResultData insertResponse = orderDao.insertOrderLite(order);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }


    @Override
    public ResultData placeOrder(CustomerOrder customerOrder) {
        ResultData result = new ResultData();
        ResultData insertResponse = customerOrderDao.insertOrder(customerOrder);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData payOrder(Order order) {
        ResultData result = new ResultData();
        ResultData updateResponse = orderDao.updateOrder(order);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData payOrder(CustomerOrder customerOrder) {
        ResultData result = new ResultData();
        ResultData updateResponse = customerOrderDao.updateOrder(customerOrder);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchOrder(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = orderDao.queryOrder(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Order>) queryResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(queryResponse.getData());
            }
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchCustomerOrder(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = customerOrderDao.queryOrder(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<CustomerOrder>) queryResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(queryResponse.getData());
            }
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchCustomerOrder(Map<String, Object> condition, MobilePageParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = customerOrderDao.queryOrderByPage(condition,
                param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchCustomerOrder(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = customerOrderDao.queryOrderByPage(condition,
                param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData modifyOrder(Order order) {
        ResultData result = new ResultData();
        ResultData updateResponse = orderDao.updateOrder(order);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData poolOrder() {
        ResultData result = new ResultData();
        ResultData sumResponse = orderDao.sumOrder();
        result.setResponseCode(sumResponse.getResponseCode());
        if (sumResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(sumResponse.getData());
        } else {
            result.setDescription(sumResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchOrderPool(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData fetchResponse = orderPoolDao.queryOrderPool(condition);
        result.setResponseCode(fetchResponse.getResponseCode());
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(fetchResponse.getData());
        } else {
            result.setDescription(fetchResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData cancel(Order order) {
        ResultData result = new ResultData();
        ResultData cancelResponse = orderDao.updateOrderLite(order);
        result.setResponseCode(cancelResponse.getResponseCode());
        if (cancelResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(cancelResponse.getData());
        } else {
            result.setDescription(cancelResponse.getDescription());
        }
        return result;
    }
    
    @Override
    public ResultData useVouchers(Order order){
    	 ResultData result = new ResultData();
         ResultData cancelResponse = orderDao.updateOrderLite(order);
         result.setResponseCode(cancelResponse.getResponseCode());
         if (cancelResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
             result.setData(cancelResponse.getData());
         } else {
             result.setDescription(cancelResponse.getDescription());
         }
         return result;
    }

    @Override
    public ResultData fetchOrder(Map<String, Object> condition, MobilePageParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = orderDao.queryOrderByPage(condition, param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchOrder(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = orderDao.queryOrderByPage(condition, param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchOrderItem(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData fetchResponse = orderItemDao.queryOrderItem(condition);
        result.setResponseCode(fetchResponse.getResponseCode());
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<OrderItem>) fetchResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(fetchResponse.getData());
        } else {
            result.setDescription(fetchResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchOrderItem(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = orderItemDao.queryOrderItemByPage(condition, param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchOrderItemSum(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData fetchResponse = orderItemDao.queryOrderItemSum(condition);
        result.setResponseCode(fetchResponse.getResponseCode());
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<OrderItemSum>) fetchResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(fetchResponse.getData());
        } else {
            result.setDescription(fetchResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchOrderItemSum(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData response = orderItemDao.queryOrderItemSum(condition, param);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData updateOrderItem(OrderItem orderItem) {
        ResultData result = new ResultData();
        ResultData updateResponse = orderItemDao.updateOrderItem(orderItem);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData updateCustomerOrder(CustomerOrder customerOrder) {
        ResultData result = new ResultData();
        ResultData updateResponse = customerOrderDao.updateOrder(customerOrder);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData updateOrderLite(Order order) {
        ResultData result = new ResultData();
        ResultData updateResponse = orderDao.updateOrderLite(order);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

    /**
     * 此方法用于扫描顾客订单,将自助购买的客户添加到代理商的顾客列表中
     * 扫描顾客订单,获取订单中的customer phone和agent_id
     * 查找该customer phone是否存在于系统的customer表中
     * 若已存在,则不需要再次插入
     * 若不存在,根据订单信息构造顾客信息,添加到customer中
     * 包括没有代理商的顾客也需要进行添加
     *
     * @return
     */
    @Override
    public ResultData check() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        //查询顾客订单
        ResultData response = customerOrderDao.queryOrder(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前未查询到顾客订单");
            return result;
        }
        //根据查询到的顾客订单进行订单处理
        List<CustomerOrder> list = (List<CustomerOrder>) response.getData();
        for (CustomerOrder item : list) {
            condition.clear();
            //查询是否有此顾客存在
            condition.put("phone", item.getReceiverPhone());
            condition.put("blockFlag", false);
            response = customerDao.queryCustomer(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                Customer customer = new Customer(item.getReceiverName(), item.getReceiverAddress(), item.getReceiverPhone());
                customer.setAgent(item.getAgent());
                response = customerDao.insertCustomer(customer);
                if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                    logger.error(response.getDescription());
                }
                CustomerVo vo = (CustomerVo) response.getData();
                item.setCustomerId(vo.getCustomerId());
                response = customerOrderDao.updateOrder(item);
                if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                    logger.error(response.getDescription());
                }
                continue;
            }
            if (StringUtils.isEmpty(item.getAgent())) {
                continue;
            }
            //此顾客之前已存在，需要判断该顾客的客服是否存在及客服类型
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                CustomerVo target = ((List<CustomerVo>) response.getData()).get(0);
                //获取顾客之前的代理商
                condition.clear();
                Agent customerAgent = null;
                if (!StringUtils.isEmpty(target.getAgent())) {
                    condition.put("agentId", target.getAgent().getAgentId());
                    response = agentDao.queryAgent(condition);
                    customerAgent = ((List<Agent>) response.getData()).get(0);
                }
                if (customerAgent != null && customerAgent.getAgentType() != AgentType.SUPPORT) {
                    continue;
                }
                //获取顾客订单中的代理商
                condition.clear();
                condition.put("agentId", item.getAgent().getAgentId());
                response = agentDao.queryAgent(condition);
                Agent orderAgent = ((List<Agent>) response.getData()).get(0);
                if (customerAgent == null || orderAgent.getAgentType() != AgentType.SUPPORT) {
                    //更新顾客的代理商
                    //将原来的顾客block
                    Customer customer = new Customer(target.getName(), target.getAddress(), target.getPhone(), target.getAgent());
                    customer.setCustomerId(target.getCustomerId());
                    response = customerDao.deleteCustomer(customer);
                    if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                        logger.error(response.getDescription());
                        continue;
                    }
                    //根据新的agent创建顾客并回填到customer order中
                    customer.setAgent(new common.sunshine.model.selling.agent.lite.Agent(orderAgent));
                    customer.setCustomerId("");
                    response = customerDao.insertCustomer(customer);
                    if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                        logger.error(response.getDescription());
                    }
                    CustomerVo vo = (CustomerVo) response.getData();
                    item.setCustomerId(vo.getCustomerId());
                    response = customerOrderDao.updateOrder(item);
                    if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                        logger.error(response.getDescription());
                    }
                }
            }

        }
        return result;
    }

    @Override
    public ResultData fullFillCusOrder() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = customerOrderDao.queryOrder(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未查询到顾客订单");
            return result;
        }
        List<CustomerOrder> list = (List<CustomerOrder>) response.getData();
        for (CustomerOrder customerOrder : list) {
            if (StringUtils.isEmpty(customerOrder.getCustomerId())) {
                condition.clear();
                condition.put("phone", customerOrder.getReceiverPhone());
                condition.put("blockFlag", false);
                response = customerDao.queryCustomer(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    CustomerVo vo = ((List<CustomerVo>) response.getData()).get(0);
                    customerOrder.setCustomerId(vo.getCustomerId());
                    customerOrderDao.updateOrder(customerOrder);
                }
            }
        }
        return result;
    }

    @Override
    public ResultData checkOrderPool(Map<String, Object> condition) {
        return orderPoolDao.checkOrderPool(condition);
    }

    @Override
    public ResultData received(Order order) {
        ResultData result = new ResultData();
        ResultData receivedResponse = orderDao.updateOrderLite(order);
        result.setResponseCode(receivedResponse.getResponseCode());
        if (receivedResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(receivedResponse.getData());
        } else {
            result.setDescription(receivedResponse.getDescription());
        }
        return result;
    }

    /**
     * 满赠活动扫描器
     *
     * @return
     */
    @Override
    public ResultData n4mScanner() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("type", EventType.PROMOTION.getCode());
        ResultData fetchEventResponse = eventDao.queryPromotionEvent(condition);
        if (fetchEventResponse.getResponseCode() != ResponseCode.RESPONSE_OK || ((List<Event>) fetchEventResponse.getData()).isEmpty()) {
            result.setDescription("未找到满赠活动");
            result.setResponseCode(fetchEventResponse.getResponseCode());
            return result;
        }
        Event event = ((List<Event>) fetchEventResponse.getData()).get(0);
        Timestamp tsStart = event.getStart();
        Timestamp tsEnd = event.getEnd();

        //这里需要获取商品PromotionConfig List！！！！！(待完成)
        condition.clear();
        condition.put("blockFlag", false);
        condition.put("eventId", event.getEventId());
        fetchEventResponse = eventDao.queryPromotionConfig(condition);
        if (fetchEventResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setDescription("未找到满赠活动配置");
            result.setResponseCode(fetchEventResponse.getResponseCode());
            return result;
        }
        List<PromotionConfig> promotionConfigs = (List<PromotionConfig>) fetchEventResponse.getData();

        //获取满赠已经生成的订单
        condition.clear();
        condition.put("eventId", event.getEventId());
        condition.put("blockFlag", false);
        ResultData fetchEventOrderResponse = eventDao.queryEventOrder(condition);
        List<EventOrder> eventOrders = new ArrayList<>();
        if (fetchEventResponse.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<EventOrder>) fetchEventOrderResponse.getData()).isEmpty()) {
            eventOrders = (List<EventOrder>) fetchEventOrderResponse.getData();
        }

        //处理代理商Order
        condition.clear();
        List<Integer> statusAgent = new ArrayList<>(Arrays.asList(OrderStatus.PAYED.getCode(), OrderStatus.PATIAL_SHIPMENT.getCode(), OrderStatus.FULLY_SHIPMENT.getCode(), OrderStatus.FINISHIED.getCode()));
        condition.put("status", statusAgent);
        condition.put("type", OrderType.ORDINARY.getCode());
        condition.put("timeStampStart", tsStart);//这两个timeStamp需要配合order.xml->query测试
        condition.put("timeStampEnd", tsEnd);
        ResultData fetchOrderResponse = orderDao.queryOrder(condition);
        if (fetchOrderResponse.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<Order>) fetchOrderResponse.getData()).isEmpty()) {
            List<Order> orders = (List<Order>) fetchOrderResponse.getData();
            for (int i = 0; i < orders.size(); i++) {
                boolean findEventOrder = false;//标志该订单是否已经生成过了EventOrder
                for (EventOrder eventOrder : eventOrders) {//寻找是否有匹配的LinkId
                    if (orders.get(i).getOrderId().equals(eventOrder.getLinkId())) {
                        findEventOrder = true;
                    }
                }
                if (!findEventOrder) {//好!发现了一个没有生成EventOrder的订单
                    //于是开始生成EventOrder
                    //先合并OrderItem
                    List<OrderItem> orderItems = orders.get(i).getOrderItems();
                    //考虑到OrderItem通常很少，这里采用对每个OrderItem，寻找有没有可合并到之前的OrderItem的，有则合并到之前的OrderItem
                    for (int j = 1; j < orderItems.size(); j++) {
                        for (int k = 0; k < j; k++) {
                            if (orderItems.get(j).getGoods().getGoodsId().equals(orderItems.get(k).getGoods().getGoodsId()) &&
                                    orderItems.get(j).getCustomer().getCustomerId().equals(orderItems.get(k).getCustomer().getCustomerId())) {
                                orderItems.get(j).setGoodsQuantity(orderItems.get(j).getGoodsQuantity() + orderItems.get(k).getGoodsQuantity());
                                orderItems.get(j).setOrderItemPrice(orderItems.get(j).getOrderItemPrice() + orderItems.get(k).getOrderItemPrice());
                                orderItems.remove(j);
                                j--;
                                break;
                            }
                        }
                    }
                    //合并完成，对每个OrderItem，生成拥有同一个linkId的EventOrder
                    for (OrderItem orderItem : orderItems) {
                        for (PromotionConfig promotionConfig : promotionConfigs) {
                            if (promotionConfig.getBuyGoods().getGoodsId().equals(orderItem.getGoods().getGoodsId())) {
                                EventOrder eventOrder = new EventOrder();
                                eventOrder.setDoneeAddress(orderItem.getCustomer().getAddress().getAddress());
                                eventOrder.setDoneeName(orderItem.getCustomer().getName());
                                eventOrder.setDoneePhone(orderItem.getCustomer().getPhone().getPhone());
                                eventOrder.setEvent(event);
                                eventOrder.setGoods(promotionConfig.getGiveGoods());
                                eventOrder.setLinkId(orders.get(i).getOrderId());
                                if (promotionConfig.getFull() != 0 && orderItem.getGoodsQuantity() >= promotionConfig.getCriterion()) {
                                    eventOrder.setQuantity(orderItem.getGoodsQuantity() * promotionConfig.getGive() / promotionConfig.getFull());
                                } else {
                                    break;
                                }
                                eventOrder.setStatus(OrderItemStatus.PAYED);
                                eventDao.insertEventOrder(eventOrder);//这里报错无法回滚，需检查xml，表结构，字段类型是否正确
                            }
                        }
                    }
                }
            }
        }

        //处理客户订单
        condition.clear();
        List<Integer> statusCustomer = new ArrayList<>(Arrays.asList(OrderItemStatus.PAYED.getCode(), OrderItemStatus.SHIPPED.getCode(), OrderItemStatus.RECEIVED.getCode()));
        condition.put("status", statusCustomer);
        condition.put("timeStampStart", tsStart);//这两个timeStamp需要配合order.xml->query测试
        condition.put("timeStampEnd", tsEnd);
        ResultData fetchCustomerOrderResponse = customerOrderDao.queryOrder(condition);
        if (fetchCustomerOrderResponse.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<CustomerOrder>) fetchCustomerOrderResponse.getData()).isEmpty()) {
            List<CustomerOrder> customerOrders = (List<CustomerOrder>) fetchCustomerOrderResponse.getData();
            for (CustomerOrder customerOrder : customerOrders) {
                boolean findEventOrder = false;//标志该订单是否已经生成过了EventOrder
                for (EventOrder eventOrder : eventOrders) {//寻找是否有匹配的LinkId
                    if (customerOrder.getOrderId().equals(eventOrder.getLinkId())) {
                        findEventOrder = true;
                    }
                }
                if (!findEventOrder) {//好!发现了一个没有生成EventOrder的订单
                    for (PromotionConfig promotionConfig : promotionConfigs) {
                        if (promotionConfig.getBuyGoods().getGoodsId().equals(customerOrder.getGoods().getGoodsId())) {
                            EventOrder eventOrder = new EventOrder();
                            eventOrder.setDoneeAddress(customerOrder.getReceiverAddress());
                            eventOrder.setDoneeName(customerOrder.getReceiverName());
                            eventOrder.setDoneePhone(customerOrder.getReceiverPhone());
                            eventOrder.setEvent(event);
                            eventOrder.setGoods(promotionConfig.getGiveGoods());
                            eventOrder.setLinkId(customerOrder.getOrderId());
                            if (promotionConfig.getFull() != 0 && customerOrder.getQuantity() >= promotionConfig.getCriterion()) {
                                eventOrder.setQuantity(customerOrder.getQuantity() * promotionConfig.getGive() / promotionConfig.getFull());
                            } else {
                                break;
                            }
                            eventOrder.setStatus(OrderItemStatus.PAYED);
                            eventDao.insertEventOrder(eventOrder);//这里报错无法回滚，需检查xml，表结构，字段类型是否正确
                        }
                    }
                }
            }
        }
        return result;
    }

}
