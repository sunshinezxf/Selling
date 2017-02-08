package promotion.sunshine.service;

import common.sunshine.utils.ResultData;
import promotion.sunshine.model.Keyword;

import java.util.Map;

/**
 * Created by wxd on 2017/2/8.
 */
public interface KeywordService {

    ResultData insertKeyword(Keyword keyword);

    ResultData fetchKeyword(Map<String,Object> condition);

}
