package selling.sunshine.model;

import selling.sunshine.model.goods.Goods4Agent;
import selling.sunshine.model.goods.Goods4Customer;

/**
 * Created by sunshine on 4/8/16.
 */
public class OrderItem extends Entity {
    private String orderItemId;
    private OrderItemStatus status;
    private int goodsQuantity;
    private double orderItemPrice;
    private String receiveAddress;
    private Customer customer;
    private Goods4Agent goods;
    private Order order;

    public OrderItem() {
        super();
        this.status = OrderItemStatus.NOT_PAYED;
    }

    public OrderItem(String customerId, String goodsId, int goodsQuantity, double orderItemPrice) {
        this();
        customer = new Customer();
        customer.setCustomerId(customerId);
        goods = new Goods4Customer();
        goods.setGoodsId(goodsId);
        this.goodsQuantity = goodsQuantity;
        this.orderItemPrice = orderItemPrice;
    }

    public OrderItem(String customerId, String goodsId, int goodsQuantity, double orderItemPrice, String receiveAddress) {
        this(customerId, goodsId, goodsQuantity, orderItemPrice);
        this.receiveAddress = receiveAddress;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Goods4Agent getGoods() {
        return goods;
    }

    public void setGoods(Goods4Agent goods) {
        this.goods = goods;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }
}
