package selling.sunshine.vo.order;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.agent.lite.Agent;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.model.selling.order.support.OrderType;

/**
 * 订单项统计信息vo类
 * @author wxd
 */
public class OrderItemSum extends Entity {
    private Agent agent;//关联的代理商
    private Goods4Agent goods;//购买的商品
    private OrderType orderType;//订单类别
    private String orderId;//关联的订单ID
    private OrderItemStatus status;//订单项状态
    private int goodsQuantity;//购买的商品数量
    private double orderItemPrice;//购买的商品总价
    private String customerName;//购买的顾客姓名

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Goods4Agent getGoods() {
        return goods;
    }

    public void setGoods(Goods4Agent goods) {
        this.goods = goods;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public int getGoodsQuantity() {
        return goodsQuantity;
    }

    public void setGoodsQuantity(int goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }

    public double getOrderItemPrice() {
        return orderItemPrice;
    }

    public void setOrderItemPrice(double orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}
