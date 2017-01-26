package selling.sunshine.dao.impl;

import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.coupon.Coupon;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import selling.sunshine.dao.CouponDao;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 2017/1/25.
 */
@Repository
public class CouponDaoImpl extends BaseDao implements CouponDao {
    private Logger logger = LoggerFactory.getLogger(CouponDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertCoupon(Coupon coupon) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                coupon.setCouponId(IDGenerator.generate("CPN"));
                sqlSession.insert("selling.coupon.insert", coupon);
                result.setData(coupon);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
            return result;
        }
    }

    @Override
    public ResultData insertCoupon(List<Coupon> coupons) {
        ResultData result = new ResultData();

        return result;
    }

    @Override
    public ResultData fetchCoupon(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Coupon> list = sqlSession.selectList("selling.coupon.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData updateCoupon(Coupon coupon) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("selling.coupon.update", coupon);
            result.setData(coupon);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
