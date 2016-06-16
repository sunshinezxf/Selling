package selling.sunshine.service;

import org.springframework.stereotype.Service;
import selling.sunshine.model.Goods;
import selling.sunshine.model.GoodsThumbnail;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
@Service
public interface CommodityService {
    ResultData createCommodity(Goods goods);

    ResultData fetchCommodity(Map<String, Object> condition, DataTableParam param);
    
    ResultData fetchCommodity(Map<String, Object> condition);
    
    ResultData updateCommodity(Goods goods);

    ResultData saveCommodityThumbnails(List<GoodsThumbnail> thumbnails);

    ResultData createThumbnail(GoodsThumbnail thumbnail);
}
