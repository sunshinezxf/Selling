package common.sunshine.model.selling.event.support;

/**
 * 该类为活动类型枚举类
 * Created by sunshine on 2016/12/16.
 */
public enum EventType {
    /* 赠送活动, 买赠活动 */
    GIFT(0), PROMOTION(1);

    private int code;

    EventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
