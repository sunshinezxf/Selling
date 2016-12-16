package common.sunshine.model.selling.event.support;

/**
 * Created by sunshine on 2016/12/16.
 */
public enum EventType {
    GIFT(0), PROMOTION(1);

    private int code;

    EventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
