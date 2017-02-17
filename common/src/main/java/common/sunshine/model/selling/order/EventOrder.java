package common.sunshine.model.selling.order;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.event.Event;
import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.order.support.OrderItemStatus;

/**
 * 该类为活动生成的订单
 * Created by sunshine on 8/26/16.
 */
public class EventOrder extends Entity {
    private String orderId;

    /* 关联的购买订单编号 */
    private String linkId;

    /* 受赠人姓名 */
    private String doneeName;

    /* 受赠人手机号 */
    private String doneePhone;

    /* 受赠人收货地址 */
    private String doneeAddress;

    /* 订单项状态 */
    private OrderItemStatus status;

    /* 数量 */
    private int quantity;

    /* 关联的赠送申请 */
    private EventApplication application;

    /* 关联的活动 */
    private Event event;

    /* 赠送的商品 */
    private Goods4Customer goods;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getDoneeName() {
        return doneeName;
    }

    public void setDoneeName(String doneeName) {
        this.doneeName = doneeName;
    }

    public String getDoneePhone() {
        return doneePhone;
    }

    public void setDoneePhone(String doneePhone) {
        this.doneePhone = doneePhone;
    }

    public String getDoneeAddress() {
        return doneeAddress;
    }

    public void setDoneeAddress(String doneeAddress) {
        this.doneeAddress = doneeAddress;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public EventApplication getApplication() {
        return application;
    }

    public void setApplication(EventApplication application) {
        this.application = application;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Goods4Customer getGoods() {
        return goods;
    }

    public void setGoods(Goods4Customer goods) {
        this.goods = goods;
    }
}
