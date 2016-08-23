package selling.sunshine.service;

import common.sunshine.model.selling.bill.CustomerOrderBill;
import common.sunshine.model.selling.bill.DepositBill;
import common.sunshine.model.selling.bill.OrderBill;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/10/16.
 */
public interface BillService {
    ResultData createDepositBill(DepositBill bill,  String openId);

    ResultData fetchDepositBill(Map<String, Object> condition);

    ResultData updateDepositBill(DepositBill bill);

    ResultData createOrderBill(OrderBill bill, String openId);
    
    ResultData fetchOrderBill(Map<String, Object> condition);
    
    ResultData updateOrderBill(OrderBill bill);

    ResultData createCustomerOrderBill(CustomerOrderBill bill, String openId);
    
    ResultData fetchCustomerOrderBill(Map<String, Object> condition);
    
    ResultData updateCustomerOrderBill(CustomerOrderBill bill);
}
