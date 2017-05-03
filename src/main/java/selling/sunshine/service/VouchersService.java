package selling.sunshine.service;

import common.sunshine.model.selling.vouchers.Vouchers;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by wxd on 2017/5/2.
 */
public interface VouchersService {

    ResultData createVouchers(Vouchers vouchers);

    ResultData fetchVouchers(Map<String, Object> condition);

    ResultData useVouchers(Vouchers vouchers);

}
