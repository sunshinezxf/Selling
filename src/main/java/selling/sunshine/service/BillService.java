package selling.sunshine.service;

import selling.sunshine.model.DepositBill;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/10/16.
 */
public interface BillService {
    ResultData createDepositBill(DepositBill bill);
}
