package selling.sunshine.model.cashback.support;

/**
 * Created by sunshine on 8/10/16.
 */
public enum CashBackLevel {

    SELF(0), DIRECT(1), INDIRECT(2);

    private int code;

    CashBackLevel(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
