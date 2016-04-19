package selling.sunshine.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.CommodityDao;
import selling.sunshine.model.Goods;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 4/8/16.
 */
@Repository
public class CommodityDaoImpl extends BaseDao implements CommodityDao {

    @Transactional
    public ResultData insertCommodity(Goods goods) {
        ResultData result = new ResultData();
        Object lock = new Object();
        synchronized (lock) {
            try {
                goods.setGoodsId(IDGenerator.generate("COM"));
                sqlSession.insert("selling.goods.insert", goods);
                result.setData(goods);
            } catch (Exception e) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result = insertCommodity(goods);
            } finally {
                return result;
            }
        }
    }
}
