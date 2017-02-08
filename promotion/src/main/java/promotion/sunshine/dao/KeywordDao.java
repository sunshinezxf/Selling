package promotion.sunshine.dao;

import common.sunshine.utils.ResultData;
import promotion.sunshine.model.Keyword;

import java.util.Map;

/**
 * Created by wxd on 2017/2/7.
 */
public interface KeywordDao {

    ResultData insertKeyword(Keyword keyword);

    ResultData queryKeyword(Map<String,Object> condition);
}
