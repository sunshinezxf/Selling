package selling.sunshine.dao.impl;

import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.vouchers.Vouchers;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selling.sunshine.dao.VouchersDao;

import java.util.List;
import java.util.Map;

/**
 * Created by wxd on 2017/5/2.
 */
public class VouchersDaoImpl extends BaseDao implements VouchersDao {

    private Logger logger = LoggerFactory.getLogger(VouchersDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData queryVouchers(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
        	condition = handle(condition);
            List<Vouchers> list = sqlSession.selectList("selling.vouchers.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData insertVouchers(Vouchers vouchers) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                vouchers.setVouchersId(IDGenerator.generate("VOU"));
                sqlSession.insert("selling.vouchers.insert", vouchers);
                result.setData(vouchers);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    @Override
    public ResultData updateVouchers(Vouchers vouchers) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.vouchers.update", vouchers);
                result.setData(vouchers);
            } catch (Exception e) {
                logger.debug(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }
}

