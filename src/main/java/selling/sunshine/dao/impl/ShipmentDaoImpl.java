package selling.sunshine.dao.impl;

import common.sunshine.utils.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.ShipmentDao;
import selling.sunshine.model.ShipConfig;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/13/16.
 */
@Repository
public class ShipmentDaoImpl extends BaseDao implements ShipmentDao {

    private Logger logger = LoggerFactory.getLogger(ShipmentDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertShipmentConfig(ShipConfig config) {
        ResultData result = new ResultData();
        config.setShipConfigId(IDGenerator.generate("SCG"));
        synchronized (lock) {
            try {
                sqlSession.insert("selling.shipment.config.insert", config);
                result.setData(config);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    @Transactional
    @Override
    public ResultData insertShipmentConfig(List<ShipConfig> list) {
        ResultData result = new ResultData();
        for (ShipConfig item : list) {
            item.setShipConfigId(IDGenerator.generate("SCG"));
        }
        synchronized (lock) {
            try {
                sqlSession.delete("selling.shipment.config.delete");
                sqlSession.insert("selling.shipment.config.insertBatch", list);
                result.setData(list);
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
    public ResultData queryShipmentConfig(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<ShipConfig> list = sqlSession.selectList("selling.shipment.config.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }
}
