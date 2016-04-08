package selling.sunshine.service;

import org.springframework.stereotype.Service;
import selling.sunshine.model.Goods;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 4/8/16.
 */
@Service
public interface CommodityService {
    ResultData createCommodity(Goods goods);
}
