package selling.sunshine.service.impl;

import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.event.Event;
import common.sunshine.model.selling.event.support.EventType;
import common.sunshine.model.selling.event.support.PromotionConfig;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.pagination.MobilePageParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.*;
import selling.sunshine.service.OrderService;
import selling.sunshine.vo.order.OrderItemSum;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public ResultData check() {
        ResultData resultData = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        ResultData queryData = customerOrderDao.queryOrder(condition);
        List<CustomerOrder> customerOrders = (List<CustomerOrder>) queryData.getData();
        for (CustomerOrder customerOrder : customerOrders) {
            if (customerOrder.getAgent() != null) {
                condition.put("agentId", customerOrder.getAgent().getAgentId());
                queryData = customerDao.queryCustomer(condition);
                if (queryData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    List<Customer> customers = (List<Customer>) queryData.getData();
                    boolean flag = false;
                    for (Customer customer : customers) {
                        if (customer.getName().equals(customerOrder.getReceiverName())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        Customer newCustomer = new Customer(customerOrder.getReceiverName(), customerOrder.getReceiverAddress(),
                                customerOrder.getReceiverPhone(), customerOrder.getAgent());
                        customerDao.insertCustomer(newCustomer);
                    }
                }
            }
        }

        return resultData;
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
	 * @return
	 */
	@Override
	public ResultData n4mScanner() {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("blockFlag", false);
		condition.put("type", EventType.PROMOTION.getCode());
		ResultData fetchEventResponse = eventDao.queryPromotionEvent(condition);
		if(fetchEventResponse.getResponseCode() != ResponseCode.RESPONSE_OK || ((List<Event>)fetchEventResponse.getData()).isEmpty()){
			result.setDescription("未找到满赠活动");
			result.setResponseCode(fetchEventResponse.getResponseCode());
			return result;
		}
		Event event = ((List<Event>)fetchEventResponse.getData()).get(0);
		Timestamp tsStart = event.getStart();
		Timestamp tsEnd = event.getEnd();
		
		//这里需要获取商品PromotionConfig List！！！！！(待完成)
		condition.clear();
		condition.put("blockFlag", false);
		condition.put("eventId",event.getEventId());
		fetchEventResponse=eventDao.queryPromotionConfig(condition);
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
		List<EventOrder> eventOrders = null;
		if(fetchEventResponse.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<EventOrder>)fetchEventOrderResponse.getData()).isEmpty()){
			eventOrders = (List<EventOrder>) fetchEventResponse.getData();
		}
		
		//处理代理商Order
		condition.clear();
		List<Integer> statusAgent = new ArrayList<>();
		statusAgent.add(2);
		statusAgent.add(3);
		statusAgent.add(4);
		statusAgent.add(5);
		condition.put("status", statusAgent);
		condition.put("type", 0);
		condition.put("timeStampStart",tsStart);//这两个timeStamp需要配合order.xml->query测试
		condition.put("timeStampEnd", tsEnd);
		ResultData fetchOrderResponse = orderDao.queryOrder(condition);
		if(fetchOrderResponse.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<Order>)fetchOrderResponse.getData()).isEmpty()){
			List<Order> orders = (List<Order>) fetchOrderResponse.getData();
			for(int i = 0; i < orders.size() ; i++){
				boolean findEventOrder = false;//标志该订单是否已经生成过了EventOrder
				for(EventOrder eventOrder : eventOrders){//寻找是否有匹配的LinkId
					if(orders.get(i).getOrderId().equals(eventOrder.getLinkId())){
						findEventOrder = true;
					}
				}
				if(!findEventOrder){//好!发现了一个没有生成EventOrder的订单
					//于是开始生成EventOrder
					//先合并OrderItem
					List<OrderItem> orderItems = orders.get(i).getOrderItems();
					//考虑到OrderItem通常很少，这里采用对每个OrderItem，寻找有没有可合并到之前的OrderItem的，有则合并到之前的OrderItem
					for(int j = 1; j < orderItems.size(); j++){
						for(int k = 0; k < j; k++){
							if(orderItems.get(j).getGoods().getGoodsId().equals(orderItems.get(k).getGoods().getGoodsId()) &&
							   orderItems.get(j).getCustomer().getCustomerId().equals(orderItems.get(k).getCustomer().getCustomerId())){
							   orderItems.get(j).setGoodsQuantity(orderItems.get(j).getGoodsQuantity() + orderItems.get(k).getGoodsQuantity());
							   orderItems.get(j).setOrderItemPrice(orderItems.get(j).getOrderItemPrice() + orderItems.get(k).getOrderItemPrice());
							   orderItems.remove(j);
							   j--;
							   break;
							}
						}
					}
					//合并完成，对每个OrderItem，生成拥有同一个linkId的EventOrder
					for(OrderItem orderItem : orderItems){
						for(PromotionConfig promotionConfig : promotionConfigs){
							if(promotionConfig.getBuyGoods().getGoodsId().equals(orderItem.getGoods().getGoodsId())){
								EventOrder eventOrder = new EventOrder();
								eventOrder.setDoneeAddress(orderItem.getCustomer().getAddress().getAddress());
								eventOrder.setDoneeName(orderItem.getCustomer().getName());
								eventOrder.setDoneePhone(orderItem.getCustomer().getPhone().getPhone());
								eventOrder.setEvent(event);
								//eventOrder.setGoods(promotionConfig.getGiveGoods());
								eventOrder.setLinkId(orders.get(i).getOrderId());
								if(promotionConfig.getFull() != 0 && orderItem.getGoodsQuantity() >= promotionConfig.getCriterion()){
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
		List<Integer> statusCustomer = new ArrayList<>();
		statusCustomer.add(1);
		statusCustomer.add(2);
		statusCustomer.add(3);
		condition.put("status", statusCustomer);
		condition.put("timeStampStart",tsStart);//这两个timeStamp需要配合order.xml->query测试
		condition.put("timeStampEnd", tsEnd);
		ResultData fetchCustomerOrderResponse = customerOrderDao.queryOrder(condition);
		if(fetchCustomerOrderResponse.getResponseCode() == ResponseCode.RESPONSE_OK && !((List<CustomerOrder>)fetchCustomerOrderResponse.getData()).isEmpty()){
			List<CustomerOrder> customerOrders = (List<CustomerOrder>) fetchOrderResponse.getData();
			for(CustomerOrder customerOrder : customerOrders){
				boolean findEventOrder = false;//标志该订单是否已经生成过了EventOrder
				for(EventOrder eventOrder : eventOrders){//寻找是否有匹配的LinkId
					if(customerOrder.getOrderId().equals(eventOrder.getLinkId())){
						findEventOrder = true;
					}
				}
				if(!findEventOrder){//好!发现了一个没有生成EventOrder的订单
					for(PromotionConfig promotionConfig : promotionConfigs){
						if(promotionConfig.getBuyGoods().getGoodsId().equals(customerOrder.getGoods().getGoodsId())){
							EventOrder eventOrder = new EventOrder();
							eventOrder.setDoneeAddress(customerOrder.getReceiverAddress());
							eventOrder.setDoneeName(customerOrder.getReceiverName());
							eventOrder.setDoneePhone(customerOrder.getReceiverPhone());
							eventOrder.setEvent(event);
							//eventOrder.setGoods(promotionConfig.getGiveGoods());
							eventOrder.setLinkId(customerOrder.getOrderId());
							if(promotionConfig.getFull() != 0 && customerOrder.getQuantity() >= promotionConfig.getCriterion()){
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
