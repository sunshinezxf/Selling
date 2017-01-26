package selling.sunshine.dao;

import common.sunshine.model.selling.coupon.Coupon;
import common.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 2017/1/25.
 */
public interface CouponDao {
    ResultData insertCoupon(Coupon coupon);

    ResultData insertCoupon(List<Coupon> coupons);

    ResultData fetchCoupon(Map<String, Object> condition);

    ResultData updateCoupon(Coupon coupon);
}
