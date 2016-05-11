package selling.sunshine.model;

/**
 * Created by sunshine on 5/11/16.
 */
public enum OrderStatus {
    SUBMITTED(0), PAYED(1), PATIAL_SHIPMENT(2), FULLY_SHIPMENT(3), FINISHIED(4);

    private int code;

    OrderStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
