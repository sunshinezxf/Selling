package promotion.sunshine.service;

import common.sunshine.utils.ResultData;
import promotion.sunshine.model.Keyword;

import java.util.Map;

/**
 * Created by wxd on 2017/2/8.
 */
public interface KeywordService {
	//插入关键词
    ResultData insertKeyword(Keyword keyword);

    //查询关键词
    ResultData fetchKeyword(Map<String,Object> condition);

}
