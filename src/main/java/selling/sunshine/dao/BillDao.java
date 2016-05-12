package selling.sunshine.dao;

import java.util.Map;

import selling.sunshine.model.DepositBill;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/10/16.
 */
public interface BillDao {
    ResultData insertDepositBill(DepositBill bill);
    
    ResultData queryDepositBill(Map<String, Object> condition);
    
    ResultData updateDepositBill(DepositBill bill);
}
