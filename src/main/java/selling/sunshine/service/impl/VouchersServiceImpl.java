package selling.sunshine.service.impl;

import common.sunshine.model.selling.vouchers.Vouchers;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.dao.VouchersDao;
import selling.sunshine.service.VouchersService;

import java.util.List;
import java.util.Map;

/**
 * Created by wxd on 2017/5/2.
 */
public class VouchersServiceImpl implements VouchersService {

    private Logger logger = LoggerFactory.getLogger(VouchersServiceImpl.class);

    @Autowired
    private VouchersDao vouchersDao;

    @Override
    public ResultData createVouchers(Vouchers vouchers) {
        ResultData result = new ResultData();
        ResultData insertResponse = vouchersDao.insertVouchers(vouchers);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchVouchers(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = vouchersDao.queryVouchers(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
        	if (((List) queryResponse.getData()).isEmpty()) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			}
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData useVouchers(Vouchers vouchers) {
        ResultData result = new ResultData();
        vouchers.setUsed(true);
        ResultData updateResponse = vouchersDao.updateVouchers(vouchers);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }
}
