package common.sunshine.model.selling.coupon;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.coupon.support.CouponStatus;
import common.sunshine.model.selling.goods.Goods4Customer;

/**
 * 该类为优惠券类
 * Created by sunshine on 2017/1/25.
 */
public class Coupon extends Entity {
    private String couponId;

    /* 优惠券对应的订单编号 */
    private String orderId;

    /* 优惠券的序列号 */
    private String couponSerial;

    /* 优惠券持有人的微信号 */
    private String wechat;

    /* 优惠券的状态 */
    private CouponStatus status;

    /* 优惠券所能兑换的商品*/
    private Goods4Customer goods;

    public Coupon() {
        super();
        this.status = CouponStatus.CREATED;
    }

    public Coupon(String couponSerial) {
        this();
        this.couponSerial = couponSerial;
    }

    public Coupon(String couponSerial, CouponStatus status) {
        this(couponSerial);
        this.status = status;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCouponSerial() {
        return couponSerial;
    }

    public void setCouponSerial(String couponSerial) {
        this.couponSerial = couponSerial;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public Goods4Customer getGoods() {
        return goods;
    }

    public void setGoods(Goods4Customer goods) {
        this.goods = goods;
    }
}
