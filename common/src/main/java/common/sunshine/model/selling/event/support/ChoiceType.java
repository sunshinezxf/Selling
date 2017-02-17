package common.sunshine.model.selling.event.support;

/**
 * 该类为问题类型枚举类
 * Created by sunshine on 8/26/16.
 */
public enum ChoiceType {
    /* 单选, 多选 */
    EXCLUSIVE(0), MULTIPLE(1);

    private int code;

    ChoiceType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
