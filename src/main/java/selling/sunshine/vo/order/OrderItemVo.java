package selling.sunshine.vo.order;

import common.sunshine.model.selling.goods.AbstractGoods;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.EventOrder;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderItemStatus;

import java.sql.Timestamp;

/**
 * Created by sunshine on 2017/1/12.
 */
public class OrderItemVo {
    private String orderItemId;

    private String couponCode;

    private String customerName;

    private String customerAddress;

    private String customerPhone;

    private AbstractGoods goods;

    private int itemQuantity;

    private double itemPrice;

    private OrderItemStatus status;

    private Timestamp createAt;

    public OrderItemVo() {
        super();
    }

    public OrderItemVo(OrderItem item) {
        this();
        this.orderItemId = item.getOrderItemId();
        this.couponCode = item.getOrder().getAgent().getAgentId().substring("AGT".length());
        this.customerName = item.getCustomer().getName();
        this.customerAddress = item.getReceiveAddress();
        this.customerPhone = item.getCustomer().getPhone().getPhone();
        this.goods = item.getGoods();
        this.itemQuantity = item.getGoodsQuantity();
        this.itemPrice = item.getOrderItemPrice();
        this.status = item.getStatus();
        this.createAt = item.getCreateAt();
    }

    public OrderItemVo(CustomerOrder item) {
        this();
        this.orderItemId = item.getOrderId();
        this.couponCode = item.getAgent().getAgentId().substring("AGT".length());
        this.customerName = item.getReceiverName();
        this.customerAddress = item.getReceiverAddress();
        this.customerPhone = item.getReceiverPhone();
        this.goods = item.getGoods();
        this.itemQuantity = item.getQuantity();
        this.itemPrice = item.getTotalPrice();
        this.status = item.getStatus();
        this.createAt = item.getCreateAt();
    }

    public OrderItemVo(EventOrder item) {
        this();
        this.orderItemId = item.getOrderId();
        this.customerName = item.getDoneeName();
        this.customerAddress = item.getDoneeAddress();
        this.customerPhone = item.getDoneePhone();
        this.goods = item.getGoods();
        this.itemQuantity = item.getQuantity();
        this.itemPrice = item.getQuantity() * item.getGoods().getCustomerPrice();
        this.status = item.getStatus();
        this.createAt = item.getCreateAt();
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public AbstractGoods getGoods() {
        return goods;
    }

    public void setGoods(AbstractGoods goods) {
        this.goods = goods;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
