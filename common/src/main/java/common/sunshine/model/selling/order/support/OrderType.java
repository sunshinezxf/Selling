package common.sunshine.model.selling.order.support;

/**
 * Created by sunshine on 7/4/16.
 */
public enum OrderType {
    ORDINARY(0), GIFT(1), CUSTOMER(2), EXCHANGE(3);
    private int code;

    OrderType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
