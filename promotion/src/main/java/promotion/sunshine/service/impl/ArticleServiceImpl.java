package promotion.sunshine.service.impl;

import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import promotion.sunshine.dao.ArticleDao;
import promotion.sunshine.model.Article;
import promotion.sunshine.service.ArticleService;

/**
 * Created by wxd on 2017/2/8.
 */
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public ResultData insertArticle(Article article) {
        ResultData result = new ResultData();
        ResultData insertResponse = articleDao.insertArticle(article);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }
}
