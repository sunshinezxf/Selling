package selling.sunshine.service;

import java.util.Map;

import selling.sunshine.model.DepositBill;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/10/16.
 */
public interface BillService {
    ResultData createDepositBill(DepositBill bill);
    
    ResultData fetchDepositBill(Map<String, Object> condition);
}
