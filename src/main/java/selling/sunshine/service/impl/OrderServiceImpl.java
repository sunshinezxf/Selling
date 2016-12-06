package selling.sunshine.service.impl;

import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
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

}
