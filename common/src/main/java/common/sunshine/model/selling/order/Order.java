package common.sunshine.model.selling.order;

import com.alibaba.fastjson.JSONObject;
import common.sunshine.model.Entity;
import common.sunshine.model.selling.agent.lite.Agent;
import common.sunshine.model.selling.order.support.OrderStatus;
import common.sunshine.model.selling.order.support.OrderType;

import java.util.List;

/**
 * Created by sunshine on 4/8/16.
 */
public class Order extends Entity {
    private String orderId;
    private Agent agent;
    private double price;
    private OrderType type;
    private OrderStatus status;
    private List<OrderItem> orderItems;

    public Order() {
        super();
        this.type = OrderType.ORDINARY;
        this.status = OrderStatus.SUBMITTED;
    }

    public Order(OrderType type) {
        this();
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
