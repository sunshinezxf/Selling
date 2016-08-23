package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.ChargeDao;
import common.sunshine.model.selling.charge.Charge;
import selling.sunshine.service.ChargeService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.HashMap;
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

    @Override
    public ResultData reimburse(Charge charge) {
        ResultData result = new ResultData();
        try {
            com.pingplusplus.model.Charge target = com.pingplusplus.model.Charge.retrieve(charge.getChargeId());
            Map<String, Object> param = new HashMap<>();
            param.put("amount", target.getAmount());
            param.put("description", "商户取消您的订单,并已全额退款");
            target.getRefunds().create(param);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
