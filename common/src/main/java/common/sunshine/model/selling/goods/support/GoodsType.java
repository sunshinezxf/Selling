package common.sunshine.model.selling.goods.support;

/**
 * Created by sunshine on 2017/1/26.
 */
public enum GoodsType {

    REAL(0), VIRTUAL(1);

    private int code;

    GoodsType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

