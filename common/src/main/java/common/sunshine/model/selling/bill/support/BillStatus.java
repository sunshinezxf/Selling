package common.sunshine.model.selling.bill.support;

/**
 * 订单状态枚举类
 * Created by sunshine on 5/10/16.
 */
public enum BillStatus {
    /* 未付款, 已付款, 已退款 */
    NOT_PAYED(0), PAYED(1), REFUND(2);

    private int code;

    BillStatus(int status) {
        this.code = status;
    }

    public int getCode() {
        return code;
    }
}
