package common.sunshine.model.selling.coupon.support;

/**
 * Created by sunshine on 2017/1/25.
 */
public enum CouponStatus {
    CREATED(0), LOCKED(1), CONSUMED(2);

    int code;

    CouponStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
