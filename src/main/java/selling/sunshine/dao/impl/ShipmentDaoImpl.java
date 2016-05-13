package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.ShipmentDao;
import selling.sunshine.model.ShipConfig;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/13/16.
 */
@Repository
public class ShipmentDaoImpl extends BaseDao implements ShipmentDao {

    private Logger logger = LoggerFactory.getLogger(ShipmentDaoImpl.class);

    @Override
    public ResultData insertShipmentConfig(ShipConfig config) {
        ResultData result = new ResultData();
        config.setShipConfigId(IDGenerator.generate("SCG"));
        try {
            sqlSession.insert("selling.shipment.config.insert", config);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
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
