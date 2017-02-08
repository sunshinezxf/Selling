package promotion.sunshine.dao.impl;

import common.sunshine.dao.BaseDao;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import promotion.sunshine.dao.ArticleDao;
import promotion.sunshine.model.Article;

/**
 * Created by wxd on 2017/2/7.
 */
public class ArticleDaoImpl extends BaseDao implements ArticleDao {

    private Logger logger = LoggerFactory.getLogger(ArticleDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertArticle(Article article) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                article.setArticleId(IDGenerator.generate("ART"));
                sqlSession.insert("promotion.article.insert", article);
                result.setData(article);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }
}
