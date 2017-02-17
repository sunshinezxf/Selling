package common.sunshine.model.selling.event.support;

/**
 * 该类为申请状态枚举类
 * Created by sunshine on 8/26/16.
 */
public enum ApplicationStatus {
    /* 已申请, 已拒绝, 已通过 */
    APPLIED(0), REJECTED(1), APPROVED(2);

    private int code;

    ApplicationStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
