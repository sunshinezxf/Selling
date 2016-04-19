package selling.sunshine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.CommodityDao;
import selling.sunshine.model.Goods;
import selling.sunshine.service.CommodityService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 4/8/16.
 */
@Service
public class CommodityServiceImpl implements CommodityService {

    @Autowired
    private CommodityDao commodityDao;

    public ResultData createCommodity(Goods goods) {
        ResultData result = new ResultData();
        ResultData insertResponse = commodityDao.insertCommodity(goods);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setData(insertResponse.getDescription());
        }
        return result;
    }
}
