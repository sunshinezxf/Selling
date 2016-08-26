package common.sunshine.model.selling.event.support;

/**
 * Created by sunshine on 8/26/16.
 */
public enum ChoiceType {
    EXCLUSIVE(0), MULTIPLE(1);

    private int code;

    ChoiceType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
