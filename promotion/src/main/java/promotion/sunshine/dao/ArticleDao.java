package promotion.sunshine.dao;

import common.sunshine.utils.ResultData;
import promotion.sunshine.model.Article;

/**
 * Created by wxd on 2017/2/7.
 */
public interface ArticleDao {

    ResultData insertArticle(Article article);
}
