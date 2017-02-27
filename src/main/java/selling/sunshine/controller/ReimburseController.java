package selling.sunshine.controller;

import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import common.sunshine.model.selling.bill.CustomerOrderBill;
import common.sunshine.model.selling.order.CustomerOrder;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import selling.sunshine.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算退款的接口
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

    /**
     * 订单退款的通知
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
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
}
