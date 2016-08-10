package selling.sunshine.model;

/**
 * Created by sunshine on 8/3/16.
 */
public class Charge extends Entity {
    private String chargeId;
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
