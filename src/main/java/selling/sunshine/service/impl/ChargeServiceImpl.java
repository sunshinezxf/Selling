package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.ChargeDao;
import selling.sunshine.model.Charge;
import selling.sunshine.service.ChargeService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 8/3/16.
 */
@Service
public class ChargeServiceImpl implements ChargeService {
    private Logger logger = LoggerFactory.getLogger(ChargeServiceImpl.class);

    @Autowired
    private ChargeDao chargeDao;

    @Override
    public ResultData fectchCharge(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = chargeDao.queryCharge(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Charge>) response.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(response.getData());
        }
        return result;
    }
}
