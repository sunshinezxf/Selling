package selling.sunshine.model.gift;

/**
 * Created by sunshine on 8/5/16.
 */
public enum GiftApplyStatus {
    APPLYED(0), PROCESSED(1), REJECTED(2);

    private int code;

    GiftApplyStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
