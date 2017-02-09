package promotion.sunshine.service.impl;

import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import promotion.sunshine.dao.ArticleDao;
import promotion.sunshine.model.Article;
import promotion.sunshine.model.Keyword;
import promotion.sunshine.service.ArticleService;
import promotion.sunshine.service.KeywordService;

import java.io.*;
import java.util.*;

/**
 * Created by wxd on 2017/2/8.
 */
public class ArticleServiceImpl implements ArticleService {

    private Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private KeywordService keywordService;

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

    @Override
    public ResultData queryArticle(Map<String, Object> condition) {
        ResultData result = new ResultData();
        String message=(String)condition.get("message");
        condition.clear();
        condition.put("blockFlag",false);
        ResultData fetchResponse=keywordService.fetchKeyword(condition);
        if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;

        }
        //得到所有的关键词
        List<Keyword> keywordList = (List<Keyword>) fetchResponse.getData();
        Map<String, promotion.sunshine.model.Article> map=new HashMap<>();
        for (Keyword keyword:keywordList){
            map.put(keyword.getContent(),keyword.getArticle());
        }
        Set<Article> set = new HashSet<>();
        List<common.sunshine.model.wechat.Article> list=new ArrayList<>();

        try {
            byte[] bt = message.getBytes();
            InputStream ip = new ByteArrayInputStream(bt);
            Reader read = new InputStreamReader(ip);
            IKSegmenter iks = new IKSegmenter(read,true);//true开启只能分词模式，如果不设置默认为false，也就是细粒度分割
            Lexeme t;
            logger.debug("测试分词结果");
            //遍历分词数据
            while((t = iks.next()) != null){
                logger.debug(t.getLexemeText());
                if (map.containsKey(t.getLexemeText())){
                    set.add(map.get(t.getLexemeText()));
                }
            }
            Iterator<Article> it = set.iterator();
            while (it.hasNext()) {
                Article article = it.next();
                common.sunshine.model.wechat.Article wArticle = new common.sunshine.model.wechat.Article();
                wArticle.setTitle(article.getTitle());
                wArticle.setPicUrl(article.getPicUrl());
                wArticle.setUrl(article.getUrl());
                list.add(wArticle);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            result.setData(list);
        }
        return result;
    }
}
