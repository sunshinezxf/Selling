package promotion.sunshine.service.impl;

import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import promotion.sunshine.dao.KeywordDao;
import promotion.sunshine.model.Keyword;
import promotion.sunshine.service.KeywordService;

import java.util.List;
import java.util.Map;

/**
 * Created by wxd on 2017/2/8.
 */
public class KeywordServiceImpl implements KeywordService {

    @Autowired
    private KeywordDao keywordDao;

    @Override
    public ResultData insertKeyword(Keyword keyword) {
        ResultData result = new ResultData();
        ResultData insertResponse = keywordDao.insertKeyword(keyword);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchKeyword(Map<String,Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = keywordDao.queryKeyword(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List) queryResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }
}
