package selling.sunshine.controller;

import common.sunshine.model.selling.coupon.Coupon;
import common.sunshine.model.selling.coupon.support.CouponStatus;
import common.sunshine.model.selling.customer.Customer;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.PurchaseForm;
import selling.sunshine.service.CommodityService;
import selling.sunshine.service.CouponService;
import selling.sunshine.service.CustomerService;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.Prompt;
import selling.sunshine.utils.PromptCode;
import selling.sunshine.utils.WechatConfig;
import selling.sunshine.utils.WechatUtil;
import selling.sunshine.vo.customer.CustomerVo;

import javax.validation.Valid;
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

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private OrderService orderService;

    private Object lock = new Object();

    @RequestMapping(method = RequestMethod.GET, value = "/exchange")
    public ModelAndView coupon(String couponSerial) {
        ModelAndView view = new ModelAndView();
        if (!StringUtils.isEmpty(couponSerial)) {
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
        }
        view.setViewName("/customer/coupon/exchange");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/exchange")
    public synchronized ModelAndView coupon(@Valid PurchaseForm form, BindingResult result, String couponSerial) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "出问题了", "您的兑换申请未能成功提交", "");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/inform");
        }
        synchronized (lock) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("couponSerial", couponSerial);
            condition.put("status", CouponStatus.CREATED.getCode());
            condition.put("blockFlag", false);
            ResultData fetchResponse = couponService.fetchCoupon(condition);
            if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "出问题了", "您的兑换申请未能成功提交", "");
                view.addObject("prompt", prompt);
                view.setViewName("/customer/inform");
            }
            Coupon coupon = ((List<Coupon>) fetchResponse.getData()).get(0);
            String goodsId = form.getGoodsId();
            String customerName = form.getCustomerName();
            int goodsQuantity = 1;
            String phone = form.getPhone();
            String address = form.getAddress();
            String wechat = form.getWechat();
            //判断该顾客是否已经存在与顾客列表中
            condition.clear();
            condition.put("phone", phone);
            condition.put("blockFlag", false);
            ResultData response = customerService.fetchCustomer(condition);
            //若存在，则查出这个customer并记录到order中
            CustomerVo vo = null;
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                vo = ((List<CustomerVo>) response.getData()).get(0);
            } else {
                Customer customer = new Customer(customerName, address, phone);
                response = customerService.createCustomer(customer);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    vo = (CustomerVo) response.getData();
                } else {
                    logger.error(response.getDescription());
                }
            }
            //判断商品是否合法
            condition.clear();
            condition.put("goodsId", goodsId);
            ResultData fetchCommodityData = commodityService.fetchGoods4Customer(condition);
            if (fetchCommodityData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "出问题了", "未能找到兑换的商品", "");
                view.addObject("prompt", prompt);
                view.setViewName("/customer/inform");
            }
            Goods4Customer goods = ((List<Goods4Customer>) fetchCommodityData.getData()).get(0);
            double goodsPrice = goods.getCustomerPrice();
            double totalPrice = goodsPrice * goodsQuantity;
            CustomerOrder customerOrder = new CustomerOrder(goods, goodsQuantity, customerName, phone, address);
            customerOrder.setTotalPrice(totalPrice);
            customerOrder.setWechat(wechat);
            customerOrder.setCustomerId(vo.getCustomerId());
            customerOrder.setStatus(OrderItemStatus.PAYED);
            customerOrder.setCouponSerial(coupon.getCouponSerial());
            fetchResponse = orderService.placeOrder(customerOrder);
            if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "出问题了", "订单生成错误", "");
                view.addObject("prompt", prompt);
                view.setViewName("/customer/inform");
            }
            coupon.setStatus(CouponStatus.CONSUMED);
            couponService.updateCoupon(coupon);
            Prompt prompt = new Prompt("兑换成功", "您的兑换已完成,我们将尽快为你安排", "");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/inform");
            return view;
        }
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
