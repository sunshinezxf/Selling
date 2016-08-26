package common.sunshine.model.selling.event.support;

/**
 * Created by sunshine on 8/26/16.
 */
public enum ApplicationStatus {
    APPLIED(0), REJECTED(1), APPROVED(2);

    private int code;

    ApplicationStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
