package common.sunshine.model.selling.order.support;

/**
 * 代理商订单项状态|顾客订单状态的枚举类
 * Created by sunshine on 5/11/16.
 */
public enum OrderItemStatus {
    /* 未付款, 已付款, 已发货, 已签收, 已换货, 退款中, 已退款 */
    NOT_PAYED(0), PAYED(1), SHIPPED(2), RECEIVED(3), EXCHANGED(4), REFUNDING(5), REFUNDED(6);

    private int code;

    OrderItemStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
