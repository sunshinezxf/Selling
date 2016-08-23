package selling.sunshine.service;

import org.springframework.stereotype.Service;

import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.goods.Thumbnail;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

import java.util.List;
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
    
    ResultData fetchThumbnail(Map<String, Object> condition);
    
    ResultData fetchThumbnail();
    
    public ResultData updateThumbnails(List<Thumbnail> thumbnails);
}
