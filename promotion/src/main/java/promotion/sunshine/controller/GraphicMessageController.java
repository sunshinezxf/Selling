package promotion.sunshine.controller;

import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import promotion.sunshine.form.GraphicMessageForm;
import promotion.sunshine.model.Article;
import promotion.sunshine.model.Keyword;
import promotion.sunshine.service.ArticleService;
import promotion.sunshine.service.KeywordService;

import javax.validation.Valid;

/**
 * Created by wxd on 2017/2/7.
 */
@RestController
@RequestMapping("/graphicMessage")
public class GraphicMessageController {

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private ArticleService articleService;

    /**
     * 添加关键词与对应的图文消息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResultData register(@Valid GraphicMessageForm form,BindingResult result){
        ResultData resultData=new ResultData();
        if (result.hasErrors()) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return resultData;
        }
        Article article=new Article();
        article.setDescription(form.getDescription());
        article.setTitle(form.getTitle());
        article.setUrl(form.getUrl());
        ResultData insertResponse=articleService.insertArticle(article);
        if (insertResponse.getResponseCode() != ResponseCode.RESPONSE_OK){
            resultData.setResponseCode(insertResponse.getResponseCode());
            resultData.setResponseCode(insertResponse.getResponseCode());
            return resultData;
        }
        article=(Article)insertResponse.getData();
        String[] keywordList=form.getKeywordList();
        for (String s:keywordList){
            Keyword keyword=new Keyword();
            keyword.setArticle(article);
            keyword.setContent(s);
            insertResponse=keywordService.insertKeyword(keyword);
            if (insertResponse.getResponseCode() != ResponseCode.RESPONSE_OK){
                resultData.setResponseCode(insertResponse.getResponseCode());
                resultData.setResponseCode(insertResponse.getResponseCode());
                return resultData;
            }
        }
        return resultData;
    }

}
