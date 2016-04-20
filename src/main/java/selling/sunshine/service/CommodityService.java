package selling.sunshine.service;

import org.springframework.stereotype.Service;
import selling.sunshine.model.Goods;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
@Service
public interface CommodityService {
    ResultData createCommodity(Goods goods);

    ResultData fetchCommodity(Map<String, Object> condition, DataTableParam param);
}
