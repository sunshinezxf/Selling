package selling.sunshine.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import common.sunshine.model.selling.express.Express;
import common.sunshine.model.selling.express.Express4Agent;
import common.sunshine.model.selling.express.Express4Application;
import common.sunshine.model.selling.express.Express4Customer;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.model.selling.order.support.OrderStatus;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.dao.*;
import selling.sunshine.service.ExpressService;
import selling.sunshine.utils.DigestUtil;
import selling.sunshine.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressServiceImpl implements ExpressService {

    private Logger logger = LoggerFactory.getLogger(ExpressServiceImpl.class);

    @Autowired
    private ExpressDao expressDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CustomerOrderDao customerOrderDao;

    @Autowired
    private EventDao eventDao;

    @Override
    public ResultData createExpress(Express4Agent express) {
        ResultData result = new ResultData();
        ResultData insertResponse = expressDao.insertExpress4Agent(express);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData createExpress(Express4Customer express) {
        ResultData result = new ResultData();
        ResultData insertResponse = expressDao.insertExpress4Customer(express);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchExpress(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = expressDao.queryExpress(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Express>) queryResponse.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchExpress4Agent(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = expressDao.queryExpress4Agent(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Express4Agent>) queryResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchExpress4Customer(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = expressDao.queryExpress4Customer(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Express4Customer>) queryResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData traceExpress(String expressNo, String type) {
        ResultData result = new ResultData();
        Map<String, String> map = new HashMap<String, String>();
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(expressNo);
        String data = jsonArray.toJSONString();
        map.put("data", data);
        map.put("msg_type", type);
        try {
            map.put("data_digest", DigestUtil.digest(data, "AA076973A63D4CD2BBEFB60544FC1262", DigestUtil.UTF8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("company_id", "5be4f9cacac84d3d9dace29dd9026a09");
        try {
            result.setData(HttpUtil.post("http://japi.zto.cn/zto/api_utf8/traceInterface", "UTF-8", map));
        } catch (IOException e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    @Override
    public ResultData receiveCheck() {
        ResultData result = new ResultData();
        //查询已发货的Order
        Map<String, Object> condition = new HashMap<String, Object>();
        List<Integer> status = new ArrayList<Integer>();
        status.add(4);
        condition.put("status", status);
        ResultData fetchOrderResponse = orderDao.queryOrder(condition);
        if (fetchOrderResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(fetchOrderResponse.getResponseCode());
            result.setDescription(fetchOrderResponse.getDescription());
            logger.error(fetchOrderResponse.getDescription());
            return result;
        }
        List<Order> orders = (List<Order>) fetchOrderResponse.getData();
        //查询每个Order的OrderItems
        for (Order order : orders) {
            condition.clear();
            List<Integer> orderItemStatus = new ArrayList<Integer>();
            condition.put("status", 2);
            condition.put("orderId", order.getOrderId());
            ResultData fetchOrderItemResponse = orderItemDao.queryOrderItem(condition);
            if (fetchOrderItemResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(fetchOrderItemResponse.getResponseCode());
                result.setDescription(fetchOrderItemResponse.getDescription());
                logger.error(fetchOrderItemResponse.getDescription());
                return result;
            }
            List<OrderItem> orderItems = (List<OrderItem>) fetchOrderItemResponse.getData();
            //查询每个OrderItem的Express
            int receiveNum = 0;//记录Order里收货的OrderItem数量
            for (OrderItem orderItem : orderItems) {
                condition.clear();
                condition.put("orderItemId", orderItem.getOrderItemId());
                condition.put("blockFlag", false);
                ResultData fetchExpressResponse = expressDao.queryExpress4Agent(condition);
                if (((List<Express>) fetchExpressResponse.getData()).isEmpty()) {
                    continue;
                }
                if (fetchExpressResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                    result.setResponseCode(fetchExpressResponse.getResponseCode());
                    result.setDescription(fetchExpressResponse.getDescription());
                    logger.error(fetchExpressResponse.getDescription());
                    return result;
                }
                Express express = ((List<Express>) fetchExpressResponse.getData()).get(0);
                ResultData fetchWuliuResponse = traceExpress(express.getExpressNumber(), "LATEST");
                if (fetchWuliuResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                    result.setResponseCode(fetchWuliuResponse.getResponseCode());
                    result.setDescription(fetchWuliuResponse.getDescription());
                    logger.error(fetchWuliuResponse.getDescription());
                    return result;
                }
                JSONObject wuliu = JSONObject.parseObject((String) fetchWuliuResponse.getData());
                JSONArray wuliuInfo = wuliu.getJSONArray("data");
                if (wuliuInfo != null && !wuliuInfo.isEmpty()) {
                    JSONObject wuliuInfoInner = wuliuInfo.getJSONObject(0);
                    JSONObject traces = wuliuInfoInner.getJSONObject("traces");
                    if (traces != null && !traces.isEmpty()) {
                        String scanType = traces.getString("scanType");
                        if (scanType.equals("签收")) {
                            receiveNum++;
                            orderItem.setStatus(OrderItemStatus.RECEIVED);
                            orderItemDao.updateOrderItem(orderItem);
                        }
                    }
                }
            }
            if (orderItems.size() == receiveNum) {
                order.setStatus(OrderStatus.FINISHIED);
                orderDao.updateOrderLite(order);
            }
        }

        status.clear();
        condition.clear();
        status.add(2);
        condition.put("status", status);
        condition.put("blockFlag", false);
        ResultData fetchCustomerOrderResponse = customerOrderDao.queryOrder(condition);
        if (fetchCustomerOrderResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(fetchCustomerOrderResponse.getResponseCode());
            result.setDescription(fetchCustomerOrderResponse.getDescription());
            logger.error(fetchCustomerOrderResponse.getDescription());
            return result;
        }
        List<CustomerOrder> customerOrders = (List<CustomerOrder>) fetchCustomerOrderResponse.getData();
        for (CustomerOrder customerOrder : customerOrders) {
            condition.clear();
            condition.put("orderId", customerOrder.getOrderId());
            condition.put("blockFlag", false);
            ResultData fetchExpressResponse = expressDao.queryExpress4Customer(condition);
            if (fetchExpressResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(fetchExpressResponse.getResponseCode());
                result.setDescription(fetchExpressResponse.getDescription());
                logger.error(fetchExpressResponse.getDescription());
                return result;
            }
            if (((List<Express>) fetchExpressResponse.getData()).isEmpty()) {
                continue;
            }
            Express express = ((List<Express>) fetchExpressResponse.getData()).get(0);
            ResultData fetchWuliuResponse = traceExpress(express.getExpressNumber(), "LATEST");
            if (fetchWuliuResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(fetchWuliuResponse.getResponseCode());
                result.setDescription(fetchWuliuResponse.getDescription());
                logger.error(fetchWuliuResponse.getDescription());
                return result;
            }
            JSONObject wuliu = JSONObject.parseObject((String) fetchWuliuResponse.getData());
            JSONArray wuliuInfo = wuliu.getJSONArray("data");
            if (wuliuInfo != null && !wuliuInfo.isEmpty()) {
                JSONObject wuliuInfoInner = wuliuInfo.getJSONObject(0);
                JSONObject traces = wuliuInfoInner.getJSONObject("traces");
                if (traces != null && !traces.isEmpty()) {
                    String scanType = traces.getString("scanType");
                    if (scanType.equals("签收")) {
                        customerOrder.setStatus(OrderItemStatus.RECEIVED);
                        customerOrderDao.updateOrder(customerOrder);
                    }
                }
            }
        }

        status.clear();
        condition.clear();
        status.add(2);
        condition.put("status", status);
        condition.put("blockFlag", false);
        ResultData fetchEventOrderResponse = eventDao.queryEventOrder(condition);
        if (fetchEventOrderResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(fetchEventOrderResponse.getResponseCode());
            result.setDescription(fetchEventOrderResponse.getDescription());
            logger.error(fetchEventOrderResponse.getDescription());
            return result;
        }
        List<EventOrder> eventOrders = (List<EventOrder>) fetchEventOrderResponse.getData();
        for (EventOrder eventOrder : eventOrders) {
            condition.clear();
            condition.put("orderId", eventOrder.getOrderId());
            condition.put("blockFlag", false);
            ResultData fetchExpressResponse = expressDao.queryExpress4Application(condition);
            if (fetchExpressResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(fetchExpressResponse.getResponseCode());
                result.setDescription(fetchExpressResponse.getDescription());
                logger.error(fetchExpressResponse.getDescription());
                return result;
            }
            if (((List<Express>) fetchExpressResponse.getData()).isEmpty()) {
                continue;
            }
            Express express = ((List<Express>) fetchExpressResponse.getData()).get(0);
            ResultData fetchWuliuResponse = traceExpress(express.getExpressNumber(), "LATEST");
            if (fetchWuliuResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(fetchWuliuResponse.getResponseCode());
                result.setDescription(fetchWuliuResponse.getDescription());
                logger.error(fetchWuliuResponse.getDescription());
                return result;
            }
            JSONObject wuliu = JSONObject.parseObject((String) fetchWuliuResponse.getData());
            JSONArray wuliuInfo = wuliu.getJSONArray("data");
            if (wuliuInfo != null && !wuliuInfo.isEmpty()) {
                JSONObject wuliuInfoInner = wuliuInfo.getJSONObject(0);
                JSONObject traces = wuliuInfoInner.getJSONObject("traces");
                if (traces != null && !traces.isEmpty()) {
                    String scanType = traces.getString("scanType");
                    if (scanType.equals("签收")) {
                        eventOrder.setStatus(OrderItemStatus.RECEIVED);
                        eventDao.updateEventOrder(eventOrder);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public ResultData createExpress(Express4Application express) {
        ResultData result = new ResultData();
        ResultData insertResponse = expressDao.insertExpress4Application(express);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchExpress4Application(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = expressDao.queryExpress4Application(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Express4Customer>) queryResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchExpress(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = expressDao.queryExpressByPage(condition, param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }
}
