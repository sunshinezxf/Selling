package common.sunshine.model.selling.coupon.support;

/**
 * 该类为优惠券状态的枚举类
 * Created by sunshine on 2017/1/25.
 */
public enum CouponStatus {
    /* 已创建, 已锁定, 已消费 */
    CREATED(0), LOCKED(1), CONSUMED(2);

    int code;

    CouponStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
