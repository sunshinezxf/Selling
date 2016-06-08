package selling.sunshine.model;

/**
 * Created by sunshine on 5/10/16.
 */
public enum BillStatus {
    NOT_PAYED(0), PAYED(1), REFUND(2);

    private int code;

    BillStatus(int status) {
        this.code = status;
    }

    public int getCode() {
        return code;
    }
}
