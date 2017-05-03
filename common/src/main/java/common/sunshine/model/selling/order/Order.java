package common.sunshine.model.selling.order;

import com.alibaba.fastjson.JSONObject;
import common.sunshine.model.Entity;
import common.sunshine.model.selling.agent.lite.Agent;
import common.sunshine.model.selling.order.support.OrderStatus;
import common.sunshine.model.selling.order.support.OrderType;
import common.sunshine.model.selling.vouchers.Vouchers;

import java.util.List;

/**
 * 代理商的订单类, 一个Order可包含一个或多个OrderItem
 * Created by sunshine on 4/8/16.
 */
public class Order extends Entity {
    private String orderId;

    /* 订单所属代理商 */
    private Agent agent;

    /* 订单价格, 该价格为订单下所有OrderItem的总和 */
    private double price;
    
    /* 订单最终支付金额,该金额为price减去代金券的值*/
    private double totalPrice;

    /* 订单类型, 购买订单和赠送订单 */
    private OrderType type;

    /* 订单状态 */
    private OrderStatus status;

    /* 该订单下的订单项列表 */
    private List<OrderItem> orderItems;
    
    /* 该订单使用的代金券*/
    private Vouchers vouchers;

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
    
    public Vouchers getVouchers() {
		return vouchers;
	}

	public void setVouchers(Vouchers vouchers) {
		this.vouchers = vouchers;
	}
	
	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
