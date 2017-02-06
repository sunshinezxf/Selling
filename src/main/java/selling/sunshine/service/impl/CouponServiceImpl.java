package selling.sunshine.service.impl;

import common.sunshine.model.selling.coupon.Coupon;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.CouponDao;
import selling.sunshine.service.CouponService;

import java.util.Map;

/**
 * Created by sunshine on 2017/1/24.
 */
@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponDao couponDao;

    @Override
    public ResultData fetchCoupon(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = couponDao.fetchCoupon(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData createCoupon(Coupon coupon) {
        ResultData result = new ResultData();
        ResultData response = couponDao.insertCoupon(coupon);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData updateCoupon(Coupon coupon) {
        ResultData result = new ResultData();
        ResultData response = couponDao.updateCoupon(coupon);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        }
        return result;
    }
}
