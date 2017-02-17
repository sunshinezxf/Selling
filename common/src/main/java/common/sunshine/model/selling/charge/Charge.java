package common.sunshine.model.selling.charge;

import common.sunshine.model.Entity;

/**
 * 该类将订单与ping++的charge进行关联
 * Created by sunshine on 8/3/16.
 */
public class Charge extends Entity {
    /* ping++后台的实际chargeId */
    private String chargeId;

    /* 该charge对应的订单编号 */
    private String orderNo;

    public Charge() {
        super();
    }

    public Charge(String chargeId, String orderNo) {
        this();
        this.chargeId = chargeId;
        this.orderNo = orderNo;
    }

    public Charge(com.pingplusplus.model.Charge charge) {
        this(charge.getId(), charge.getOrderNo());
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
