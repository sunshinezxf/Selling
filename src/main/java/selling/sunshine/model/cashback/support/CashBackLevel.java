package selling.sunshine.model.cashback.support;

/**
 * Created by sunshine on 8/10/16.
 * 返现级别-自己，一级，二级
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
