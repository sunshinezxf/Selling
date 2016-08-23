package selling.sunshine.dao;

import common.sunshine.model.selling.bill.CustomerOrderBill;
import common.sunshine.model.selling.bill.DepositBill;
import common.sunshine.model.selling.bill.OrderBill;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/10/16.
 */
public interface BillDao{
    ResultData insertDepositBill(DepositBill bill);

    ResultData queryDepositBill(Map<String, Object> condition);

    ResultData updateDepositBill(DepositBill bill);

    ResultData insertOrderBill(OrderBill bill);

    ResultData queryOrderBill(Map<String, Object> condition);

    ResultData updateOrderBill(OrderBill bill);
    
    ResultData insertCustomerOrderBill(CustomerOrderBill bill);
    
    ResultData queryCustomerOrderBill(Map<String, Object> condition);
    
    ResultData updateCustomerOrderBill(CustomerOrderBill bill);
}
