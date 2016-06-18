package selling.sunshine.service;

import org.springframework.stereotype.Service;

import selling.sunshine.model.goods.Goods4Customer;
import selling.sunshine.model.goods.Thumbnail;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
@Service
public interface CommodityService {

    ResultData createGoods4Customer(Goods4Customer goods);

    ResultData fetchGoods4Customer(Map<String, Object> condition);

    ResultData fetchGoods4Customer(Map<String, Object> condition, DataTableParam param);

    ResultData fetchGoods4Agent(Map<String, Object> condition);

    ResultData updateGoods4Customer(Goods4Customer goods);

    ResultData createThumbnail(Thumbnail thumbnail);
    
    ResultData deleteGoodsThumbnail(String thumbnailId);
}
