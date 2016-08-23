package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.ShipmentDao;
import selling.sunshine.model.ShipConfig;
import selling.sunshine.service.ShipmentService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/13/16.
 */
@Service
public class ShipmentServiceImpl implements ShipmentService {
    private Logger logger = LoggerFactory.getLogger(ShipmentServiceImpl.class);

    @Autowired
    private ShipmentDao shipmentDao;

    @Override
    public ResultData createShipmentConfig(ShipConfig config) {
        ResultData result = new ResultData();
        ResultData insertResponse = shipmentDao.insertShipmentConfig(config);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData createShipmentConfig(List<ShipConfig> list) {
        ResultData result = new ResultData();
        ResultData insertResponse = shipmentDao.insertShipmentConfig(list);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchShipmentConfig(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = shipmentDao.queryShipmentConfig(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }
}
