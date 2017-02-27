package selling.sunshine.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import common.sunshine.model.selling.agent.Agent;
import common.sunshine.model.selling.bill.CustomerOrderBill;
import common.sunshine.model.selling.bill.DepositBill;
import common.sunshine.model.selling.bill.OrderBill;
import common.sunshine.model.selling.bill.support.BillStatus;
import common.sunshine.model.selling.coupon.Coupon;
import common.sunshine.model.selling.goods.AbstractGoods;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.goods.support.GoodsType;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.Order;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.model.selling.order.support.OrderStatus;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import common.sunshine.utils.SerialGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import selling.sunshine.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/10/16.
 */
@RequestMapping("/bill")
@RestController
public class BillController {

    private Logger logger = LoggerFactory.getLogger(BillController.class);

    @Autowired
    private ToolService toolService;

    @Autowired
    private BillService billService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponService couponService;

    /**
     * 充值完成pingXX的回调方法
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/inform")
    public ResultData inform(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultData resultData = new ResultData();

        JSONObject webhooks = toolService.getParams(request);
        JSONObject charge = webhooks.getJSONObject("data").getJSONObject("object");
        String dealId = charge.getString("order_no");

        //先处理错误状态
        if (StringUtils.isEmpty(dealId)) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return resultData;
        }
        Event event = Webhooks.eventParse(webhooks.toString());
        if ("charge.succeeded".equals(event.getType())) {
            response.setStatus(200);
        } else {
            response.setStatus(500);
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return resultData;
        }

        if (dealId.startsWith("DPB")) {//充值的账单
            Map<String, Object> condition = new HashMap<>();
            condition.put("billId", dealId);
            resultData = billService.fetchDepositBill(condition);
            DepositBill depositBill = ((List<DepositBill>) resultData.getData()).get(0);

            Map<String, Object> agentCondition = new HashMap<>();
            agentCondition.put("agentId", depositBill.getAgent().getAgentId());
            Agent agent = ((List<Agent>) agentService.fetchAgent(agentCondition).getData()).get(0);
            int coffer100 = (int) (agent.getCoffer() * 100);
            int amount100 = (int) (depositBill.getBillAmount() * 100);
            double cofferNew = (coffer100 + amount100) * 1.0 / 100;
            agent.setCoffer(cofferNew);
            resultData = agentService.updateAgent(agent);
            depositBill.setStatus(BillStatus.PAYED);
            resultData = billService.updateDepositBill(depositBill);

        } else if (dealId.startsWith("ODB")) {//其他方式付款的账单
            //处理账单状态
            Map<String, Object> condition = new HashMap<>();
            condition.put("billId", dealId);
            resultData = billService.fetchOrderBill(condition);
            OrderBill orderBill = ((List<OrderBill>) resultData.getData()).get(0);
            orderBill.setStatus(BillStatus.PAYED);
            resultData = billService.updateOrderBill(orderBill);

            //处理订单状态
            if (resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
                condition.clear();
                condition.put("orderId", orderBill.getOrder().getOrderId());
                ResultData orderData = orderService.fetchOrder(condition);
                Order order = null;
                if (orderData.getResponseCode() == ResponseCode.RESPONSE_OK && orderData.getData() != null) {
                    order = ((List<Order>) orderData.getData()).get(0);
                }
                double total_price_database = 0.0;
                for (OrderItem orderItem : order.getOrderItems()) {
                    total_price_database += orderItem.getOrderItemPrice();
                }
                if (orderBill.getBillAmount() >= total_price_database) {
                    for (OrderItem orderItem : order.getOrderItems()) {
                        //判断是否是礼券
                        orderItem.setStatus(OrderItemStatus.PAYED);
                        AbstractGoods goods = orderItem.getGoods();
                        if (goods.getType() == GoodsType.VIRTUAL) {
                            //生成礼品券
                            for (int i = 0; i < orderItem.getGoodsQuantity(); i++) {
                                Coupon coupon = new Coupon(SerialGenerator.generate());
                                //获取礼品券所对应的商品, 当前固定为环保装
                                Goods4Customer g = new Goods4Customer();
                                g.setGoodsId("COMyfxwez26");
                                coupon.setGoods(g);
                                condition.clear();
                                condition.put("agentId", orderBill.getAgent().getAgentId());
                                Agent agent = ((List<Agent>) agentService.fetchAgent(condition).getData()).get(0);
                                coupon.setWechat(agent.getWechat());
                                coupon.setOrderId(orderItem.getOrderItemId());
                                ResultData createResponse = couponService.createCoupon(coupon);
                            }
                            orderItem.setStatus(OrderItemStatus.RECEIVED);
                        }
                        orderItem.setCreateAt(new Timestamp(System.currentTimeMillis()));
                    }
                    order.setStatus(OrderStatus.PAYED);
                    order.setCreateAt(new Timestamp(System.currentTimeMillis()));
                    ResultData payData = orderService.payOrder(order);
                }
            }
        } else if (dealId.startsWith("COB")) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("billId", dealId);
            ResultData customerOrderBillData = billService.fetchCustomerOrderBill(condition);
            if (customerOrderBillData.getResponseCode() != ResponseCode.RESPONSE_OK || customerOrderBillData.getData() == null) {
                resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
                resultData.setDescription("获取个人账单错误");
                return resultData;
            }
            CustomerOrderBill customerOrderBill = ((List<CustomerOrderBill>) customerOrderBillData.getData()).get(0);
            customerOrderBill.setStatus(BillStatus.PAYED);
            resultData = billService.updateCustomerOrderBill(customerOrderBill);
            if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                return resultData;
            }
            condition.clear();
            condition.put("orderId", customerOrderBill.getCustomerOrder().getOrderId());
            ResultData customerOrderData = orderService.fetchCustomerOrder(condition);
            if (customerOrderData.getResponseCode() != ResponseCode.RESPONSE_OK || customerOrderData.getData() == null) {
                resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
                resultData.setDescription("获取个人订单错误");
                return resultData;
            }
            CustomerOrder customerOrder = ((List<CustomerOrder>) customerOrderData.getData()).get(0);
            if (customerOrderBill.getBillAmount() >= customerOrder.getTotalPrice()) {
                if (customerOrder.getGoods().getType() == GoodsType.VIRTUAL) {
                    //生成礼品券
                    for (int i = 0; i < customerOrder.getQuantity(); i++) {
                        Coupon coupon = new Coupon(SerialGenerator.generate());
                        //获取礼品券所对应的商品, 当前固定为环保装
                        Goods4Customer g = new Goods4Customer();
                        g.setGoodsId("COMyfxwez26");
                        coupon.setGoods(g);
                        condition.clear();
                        coupon.setWechat(customerOrder.getWechat());
                        coupon.setOrderId(customerOrder.getOrderId());
                        ResultData createResponse = couponService.createCoupon(coupon);
                        logger.debug(JSON.toJSONString(createResponse));
                    }
                }
                if (customerOrder.getGoods().getType() == GoodsType.VIRTUAL) {
                    customerOrder.setStatus(OrderItemStatus.RECEIVED);
                } else {
                    customerOrder.setStatus(OrderItemStatus.PAYED);
                }
                customerOrder.setCreateAt(new Timestamp(System.currentTimeMillis()));
                ResultData payData = orderService.payOrder(customerOrder);
                if (payData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    return payData;
                }
            }
        }
        return resultData;
    }
}
