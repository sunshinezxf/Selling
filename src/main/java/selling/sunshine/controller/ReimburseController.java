package selling.sunshine.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;

import selling.sunshine.model.Admin;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.model.Charge;
import selling.sunshine.model.CustomerOrder;
import selling.sunshine.model.CustomerOrderBill;
import selling.sunshine.model.OrderItemStatus;
import selling.sunshine.model.User;
import selling.sunshine.service.BillService;
import selling.sunshine.service.ChargeService;
import selling.sunshine.service.LogService;
import selling.sunshine.service.OrderService;
import selling.sunshine.service.ToolService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

/**
 * Created by sunshine on 8/3/16.
 */
@RestController
@RequestMapping("/reimburse")
public class ReimburseController {
    private Logger logger = LoggerFactory.getLogger(ReimburseController.class);

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private BillService billService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private LogService logService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/inform")
    public ResultData inform(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultData result = new ResultData();
        JSONObject webhooks = toolService.getParams(request);
        JSONObject reimburse = webhooks.getJSONObject("data").getJSONObject("object");
        String dealId = reimburse.getString("order_no");
        if (StringUtils.isEmpty(dealId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        Event event = Webhooks.eventParse(webhooks.toString());
        if ("refund.succeeded".equals(event.getType())) {
            response.setStatus(200);
        } else {
            response.setStatus(500);
        }
        if (dealId.startsWith("CUO")) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("billId", dealId);
            ResultData fetchResponse = billService.fetchCustomerOrderBill(condition);
            if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                CustomerOrderBill bill = ((List<CustomerOrderBill>) fetchResponse.getData()).get(0);
                CustomerOrder order = bill.getCustomerOrder();
                if (order.getStatus() == OrderItemStatus.REFUNDING) {
                    order.setStatus(OrderItemStatus.REFUNDED);
                    orderService.updateCustomerOrder(order);
                }
            }
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/apply")
    public ResultData reimburse(HttpServletRequest request, String orderId) {
        ResultData result = new ResultData();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        Admin admin = user.getAdmin();
        BackOperationLog backOperationLog = new BackOperationLog(
                admin.getUsername(), toolService.getIP(request), "管理员" + admin.getUsername() + "发起对客户订单:" + orderId + "退款处理");
        logService.createbackOperationLog(backOperationLog);

        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("status", 1);
        ResultData response = billService.fetchCustomerOrderBill(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }
        CustomerOrderBill bill = ((List<CustomerOrderBill>) response.getData()).get(0);
        condition.clear();
        condition.put("orderNo", bill.getBillId());
        response = chargeService.fectchCharge(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }
        Charge charge = ((List<Charge>) response.getData()).get(0);
        response = chargeService.reimburse(charge);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }
        condition.clear();
        condition.put("orderId", orderId);
        response = orderService.fetchCustomerOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            CustomerOrder current = ((List<CustomerOrder>) response.getData()).get(0);
            current.setStatus(OrderItemStatus.REFUNDING);
            orderService.updateCustomerOrder(current);
        }
        return result;
    }
}
