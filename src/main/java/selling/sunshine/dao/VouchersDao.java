package selling.sunshine.dao;

import common.sunshine.model.selling.vouchers.Vouchers;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by wxd on 2017/5/2.
 */
public interface VouchersDao {

    ResultData queryVouchers(Map<String, Object> condition);

    ResultData insertVouchers(Vouchers vouchers);

    ResultData updateVouchers(Vouchers vouchers);
}
