package common.sunshine.model.selling.order;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.agent.lite.Agent;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.order.support.OrderItemStatus;

/**
 * 该类为顾客订单类
 * Created by sunshine on 6/14/16.
 */
public class CustomerOrder extends Entity {
    private String orderId;

    /* 订单中的商品 */
    private Goods4Customer goods;

    /* 优惠码所属代理商 */
    private Agent agent;

    /* 购买商品数量 */
    private int quantity;

    /* 顾客购买微信号openId */
    private String wechat;

    private String customerId;

    /* 收货人姓名 */
    private String receiverName;

    /* 收货人手机号 */
    private String receiverPhone;

    /* 收货地址 */
    private String receiverAddress;

    /* 总价格 */
    private double totalPrice;

    /* 兑换的礼券编号 */
    private String couponSerial;

    /* 订单状态 */
    private OrderItemStatus status;

    public CustomerOrder() {
        super();
        this.status = OrderItemStatus.NOT_PAYED;
    }

    public CustomerOrder(Goods4Customer goods, int quantity, String receiverName, String receiverPhone, String receiverAddress) {
        this();
        this.goods = goods;
        this.quantity = quantity;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
    }

    public CustomerOrder(Goods4Customer goods, int quantity, String receiverName, String receiverPhone, String receiverAddress, Agent agent) {
        this(goods, quantity, receiverName, receiverPhone, receiverAddress);
        this.agent = agent;
    }

    public CustomerOrder(Goods4Customer goods, int quantity, String receiverName, String receiverPhone, String receiverAddress, Agent agent, OrderItemStatus status) {
        this(goods, quantity, receiverName, receiverPhone, receiverAddress, agent);
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Goods4Customer getGoods() {
        return goods;
    }

    public void setGoods(Goods4Customer goods) {
        this.goods = goods;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCouponSerial() {
        return couponSerial;
    }

    public void setCouponSerial(String couponSerial) {
        this.couponSerial = couponSerial;
    }
}
