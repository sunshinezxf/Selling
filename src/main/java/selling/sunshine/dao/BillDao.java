package selling.sunshine.dao;

import selling.sunshine.model.DepositBill;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/10/16.
 */
public interface BillDao {
    ResultData insertDepositBill(DepositBill bill);
}
