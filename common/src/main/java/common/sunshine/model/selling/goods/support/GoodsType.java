package common.sunshine.model.selling.goods.support;

/**
 * 商品类型枚举类
 * Created by sunshine on 2017/1/26.
 */
public enum GoodsType {
    /* 实物商品, 虚拟商品 */
    REAL(0), VIRTUAL(1);

    private int code;

    GoodsType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

