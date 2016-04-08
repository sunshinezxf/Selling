package selling.sunshine.dao;

import selling.sunshine.model.Goods;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 4/8/16.
 */
public interface CommodityDao {
    ResultData insertCommodity(Goods goods);
}
