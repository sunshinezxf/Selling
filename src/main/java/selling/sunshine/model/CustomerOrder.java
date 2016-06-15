package selling.sunshine.model;

import selling.sunshine.model.lite.Agent;
/**
 * Created by sunshine on 6/14/16.
 */
public class CustomerOrder extends Entity {
    private String orderId;
    private Goods goods;
    private Agent agent;
    private int quantity;
    private String wechat;
    private String receiverName;
    private String receiverAddress;
    private double totalPrice;
    private OrderItemStatus status;

    public CustomerOrder() {
        super();
        this.status = OrderItemStatus.NOT_PAYED;
    }

    public CustomerOrder(Goods goods, int quantity, String receiverName, String receiverAddress) {
        this();
        this.goods = goods;
        this.quantity = quantity;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
    }

    public CustomerOrder(Goods goods, int quantity, String receiverName, String receiverAddress, Agent agent) {
        this(goods, quantity, receiverName, receiverAddress);
        this.agent = agent;
    }

    public CustomerOrder(Goods goods, int quantity, String receiverName, String receiverAddress, Agent agent, OrderItemStatus status) {
        this(goods, quantity, receiverName, receiverAddress, agent);
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
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
}
