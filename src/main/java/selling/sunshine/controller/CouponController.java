package selling.sunshine.controller;

import common.sunshine.model.selling.coupon.Coupon;
import common.sunshine.model.selling.coupon.support.CouponStatus;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.service.CouponService;
import selling.sunshine.utils.WechatConfig;
import selling.sunshine.utils.WechatUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 2017/1/24.
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {
    private Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private CouponService couponService;

    @RequestMapping(method = RequestMethod.GET, value = "/exchange")
    public ModelAndView coupon(String couponSerial) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        condition.put("couponSerial", couponSerial);
        condition.put("status", CouponStatus.CREATED.getCode());
        ResultData response = couponService.fetchCoupon(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("/customer/coupon/exchange");
            return view;
        }
        WechatConfig.oauthWechat(view, "/coupon/exchange?couponSerial=" + couponSerial);
        Coupon coupon = ((List<Coupon>) response.getData()).get(0);
        view.addObject("coupon", coupon);
        view.setViewName("/customer/coupon/exchange");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/exchange")
    public ModelAndView coupon(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("/customer/coupon/exchange");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ModelAndView list(String code) {
        ModelAndView view = new ModelAndView();
        if (!StringUtils.isEmpty(code)) {
            logger.debug("code: " + code);
            String openId = WechatUtil.queryOauthOpenId(code);
            Map<String, Object> condition = new HashMap<>();
            logger.debug("openId: " + openId);
            condition.put("wechat", openId);
            condition.put("blockFlag", false);
            condition.put("status", CouponStatus.CREATED.getCode());
            ResultData response = couponService.fetchCoupon(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<Coupon> list = (List<Coupon>) response.getData();
                view.addObject("coupons", list);
            }
        }
        view.setViewName("/customer/coupon/list");
        return view;
    }
}
