package promotion.sunshine.service;

import common.sunshine.utils.ResultData;
import promotion.sunshine.model.Article;

import java.util.Map;

/**
 * Created by wxd on 2017/2/8.
 */
public interface ArticleService {

	//插入文章
    ResultData insertArticle(Article article);

    //查询文章
    ResultData queryArticle(Map<String,Object> condition);
}
