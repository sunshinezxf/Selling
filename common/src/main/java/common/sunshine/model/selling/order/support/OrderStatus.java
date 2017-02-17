package common.sunshine.model.selling.order.support;

/**
 * 代理商订单状态枚举类
 * Created by sunshine on 5/11/16.
 */
public enum OrderStatus {
    /*已保存, 已提交, 已付款, 部分发货, 完全发货, 已结束*/
    SAVED(0), SUBMITTED(1), PAYED(2), PATIAL_SHIPMENT(3), FULLY_SHIPMENT(4), FINISHIED(5);

    private int code;

    OrderStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
