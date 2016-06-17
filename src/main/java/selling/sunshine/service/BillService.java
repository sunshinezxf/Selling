package selling.sunshine.service;

import selling.sunshine.model.CustomerOrderBill;
import selling.sunshine.model.DepositBill;
import selling.sunshine.model.OrderBill;
import selling.sunshine.utils.ResultData;

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

    ResultData createCustomerOrderBill(CustomerOrderBill bill);
    
    ResultData fetchCustomerOrderBill(Map<String, Object> condition);
    
    ResultData updateCustomerOrderBill(CustomerOrderBill bill);
}
