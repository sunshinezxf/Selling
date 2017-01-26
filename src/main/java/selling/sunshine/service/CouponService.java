package selling.sunshine.service;

import common.sunshine.model.selling.coupon.Coupon;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 2017/1/24.
 */
public interface CouponService {
    ResultData fetchCoupon(Map<String, Object> condition);

    ResultData createCoupon(Coupon coupon);
}
