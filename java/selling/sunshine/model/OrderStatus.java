package selling.sunshine.model;

/**
 * Created by sunshine on 5/11/16.
 */
public enum OrderStatus {
    SAVED(0), SUBMITTED(1), PAYED(2), PATIAL_SHIPMENT(3), FULLY_SHIPMENT(4), FINISHIED(5);

    private int code;

    OrderStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
