package selling.sunshine.controller;

import common.sunshine.model.selling.bill.CustomerOrderBill;
import common.sunshine.model.selling.bill.DepositBill;
import common.sunshine.model.selling.bill.OrderBill;
import common.sunshine.model.selling.bill.support.BillStatus;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.service.BillService;
import selling.sunshine.utils.Prompt;
import selling.sunshine.utils.PromptCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 6/16/16.
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private BillService billService;

    /**
     * 使用支付宝付款后的订单状态页面
     *
     * @param billId
     * @param status
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{billId}/result/{status}")
    public ModelAndView prompt(@PathVariable("billId") String billId, @PathVariable("status") String status) {
        ModelAndView view = new ModelAndView();
        if (StringUtils.isEmpty(billId)) {
            Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您好,您当前的订单号异常,请重新尝试", "/commodity/list");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/prompt");
            return view;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("billId", billId);
        if (billId.startsWith("COB")) {
            ResultData response = billService.fetchCustomerOrderBill(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您当前的订单号异常,请重新尝试", "/commodity/list");
                view.addObject("prompt", prompt);
                view.setViewName("/customer/prompt");
                return view;
            }
            CustomerOrderBill bill = ((List<CustomerOrderBill>) response.getData()).get(0);
            if (bill.getStatus() == BillStatus.PAYED) {
                Prompt prompt = new Prompt("支付成功", "您的订单已付款成功,点击查看详情", "/commodity/customerorder?orderId=" + bill.getCustomerOrder().getOrderId());
                view.addObject("prompt", prompt);
                view.addObject("orderId", bill.getCustomerOrder().getOrderId());
                view.setViewName("/customer/prompt");
                return view;
            }
            if (bill.getStatus() == BillStatus.NOT_PAYED && (StringUtils.isEmpty(status) || (!StringUtils.isEmpty(status) && status.equals("success")))) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "支付处理中", "您的订单付款尚在处理中,请稍后核对订单状态", "/commodity/viewlist");
                view.addObject("prompt", prompt);
                view.setViewName("/customer/prompt");
                return view;
            }
            if (bill.getStatus() == BillStatus.NOT_PAYED && (!StringUtils.isEmpty(status) && status.equals("failure"))) {
                Prompt prompt = new Prompt(PromptCode.DANGER, "支付失败", "您已取消支付", "/commodity/viewlist");
                view.addObject("prompt", prompt);
                view.setViewName("/customer/prompt");
                return view;
            }
            Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您的订单尚未完成支付，请付款后进行查询", "/commodity/viewlist");
            view.addObject("prompt", prompt);
            view.setViewName("/customer/prompt");
            return view;
        }
        if (billId.startsWith("ODB")) {
            ResultData response = billService.fetchOrderBill(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您当前的订单号异常,请重新尝试", "/agent/order/place");
                view.addObject("prompt", prompt);
                view.setViewName("/customer/prompt");
                return view;
            }
            OrderBill bill = ((List<OrderBill>) response.getData()).get(0);
            if (bill.getStatus() == BillStatus.PAYED) {
                Prompt prompt = new Prompt("支付成功", "您的订单已付款成功,点击查看订单列表", "/agent/order/manage/2");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            if (bill.getStatus() == BillStatus.NOT_PAYED && (StringUtils.isEmpty(status) || (!StringUtils.isEmpty(status) && status.equals("success")))) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "支付处理中", "您的订单付款尚在处理中,请稍后核对订单状态", "/agent/order/manage/2");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            if (bill.getStatus() == BillStatus.NOT_PAYED && (!StringUtils.isEmpty(status) && status.equals("failure"))) {
                Prompt prompt = new Prompt(PromptCode.DANGER, "支付失败", "您已取消付款", "/agent/order/place");
                view.addObject("prompt", prompt);
                return view;
            }
            Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您的订单尚未完成支付,请付款后进行查询", "/agent/order/place");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
        if (billId.startsWith("DPB")) {
            ResultData response = billService.fetchDepositBill(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您的账户充值失败,请重新尝试", "/account/deposit");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            DepositBill bill = ((List<DepositBill>) response.getData()).get(0);
            if (bill.getStatus() == BillStatus.PAYED) {
                Prompt prompt = new Prompt("支付成功", "您已成功充值", "/account/info");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            if (bill.getStatus() == BillStatus.NOT_PAYED && (StringUtils.isEmpty(status) || (!StringUtils.isEmpty(status) && status.equals("success")))) {
                Prompt prompt = new Prompt(PromptCode.WARNING, "支付处理中", "您的充值尚在处理中,请稍后核对订单状态", "/agent/order/manage/2");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            if (bill.getStatus() == BillStatus.NOT_PAYED && (!StringUtils.isEmpty(status) && status.equals("failure"))) {
                Prompt prompt = new Prompt(PromptCode.DANGER, "支付失败", "您已取消付款", "/agent/account/info");
                view.addObject("prompt", prompt);
                view.setViewName("/agent/prompt");
                return view;
            }
            Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您的充值尚未完成支付,请付款后进行查询", "/agent/order/place");
            view.addObject("prompt", prompt);
            view.setViewName("/agent/prompt");
            return view;
        }
        Prompt prompt = new Prompt(PromptCode.WARNING, "系统提醒", "您好,您当前的订单号异常,请重新尝试", "/commodity/list");
        view.addObject("prompt", prompt);
        view.setViewName("/customer/prompt");
        return view;
    }
}
