package common.sunshine.model.selling.order.support;

/**
 * Created by sunshine on 5/11/16.
 */
public enum OrderItemStatus {
    NOT_PAYED(0), PAYED(1), SHIPPED(2), RECEIVED(3), EXCHANGED(4), REFUNDING(5), REFUNDED(6);

    private int code;

    OrderItemStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
