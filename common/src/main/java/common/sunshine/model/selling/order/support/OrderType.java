package common.sunshine.model.selling.order.support;

/**
 * 订单类型
 * Created by sunshine on 7/4/16.
 */
public enum OrderType {
    /* 代理商购买订单, 代理商赠送订单, 顾客购买订单, 兑换订单*/
    ORDINARY(0), GIFT(1), CUSTOMER(2), EXCHANGE(3);
    private int code;

    OrderType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
