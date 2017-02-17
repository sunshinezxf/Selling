package promotion.sunshine.dao;

import common.sunshine.utils.ResultData;
import promotion.sunshine.model.Keyword;

import java.util.Map;

/**
 * Created by wxd on 2017/2/7.
 */
public interface KeywordDao {
	
	//插入关键词
    ResultData insertKeyword(Keyword keyword);

    //查询关键词
    ResultData queryKeyword(Map<String,Object> condition);
}
