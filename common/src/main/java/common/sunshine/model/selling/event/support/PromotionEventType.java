package common.sunshine.model.selling.event.support;

/**
 * 该类为满赠活动针对人群枚举类
 * Created by wxd on 2017/2/27.
 */
public enum PromotionEventType {

    /*所有，男性，女性*/
    ALL(0),MALE(1),FEMALE(2);

    private int code;

    PromotionEventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
