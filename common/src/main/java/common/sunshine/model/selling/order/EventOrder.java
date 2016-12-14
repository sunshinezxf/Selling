package common.sunshine.model.selling.order;

import common.sunshine.model.Entity;

import common.sunshine.model.selling.event.Event;
import common.sunshine.model.selling.event.EventApplication;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.order.support.OrderItemStatus;

/**
 * Created by sunshine on 8/26/16.
 */
public class EventOrder extends Entity {
    private String orderId;

    private String doneeName;

    private String doneePhone;

    private String doneeAddress;

    private OrderItemStatus status;

    private int quantity;

    private EventApplication application;

    private Event event;

    private Goods4Customer goods;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
