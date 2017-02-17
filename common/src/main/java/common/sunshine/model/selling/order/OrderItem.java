package common.sunshine.model.selling.order;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.customer.CustomerPhone;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.order.support.OrderItemStatus;

/**
 * 订单项
 * Created by sunshine on 4/8/16.
 */
public class OrderItem extends Entity {
    private String orderItemId;

    /* 订单项状态 */
    private OrderItemStatus status;

    /* 商品数量 */
    private int goodsQuantity;

    /* 订单项的价格 */
    private double orderItemPrice;

    /* 收件人地址 */
    private String receiveAddress;

    /* 备注信息 */
    private String description;

    /* 所属顾客 */
    private Customer customer;

    /* 订单中的商品 */
    private Goods4Agent goods;

    /* 所属订单 */
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

    public OrderItem(String customerId, String goodsId, int goodsQuantity, double orderItemPrice, String receiveAddress, String description) {
        this(customerId, goodsId, goodsQuantity, orderItemPrice, receiveAddress);
        this.description = description;
    }

    public OrderItem(CustomerOrder customerOrder) {
        this.orderItemId = customerOrder.getOrderId();
        this.status = customerOrder.getStatus();
        this.goodsQuantity = customerOrder.getQuantity();
        this.orderItemPrice = customerOrder.getTotalPrice();
        this.receiveAddress = customerOrder.getReceiverAddress();
        Customer customer = new Customer();
        CustomerPhone phone = new CustomerPhone();
        phone.setPhone(customerOrder.getReceiverPhone());
        customer.setName(customerOrder.getReceiverName());
        customer.setPhone(phone);
        this.customer = customer;
        this.goods = new Goods4Agent(customerOrder.getGoods());
        this.setCreateAt(customerOrder.getCreateAt());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
