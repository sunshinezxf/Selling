package promotion.sunshine.controller;

import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import promotion.sunshine.service.ArticleService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wxd on 2017/2/9.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ArticleService articleService;

    /**
     * 测试微信端用户输入的语句的分词及分词之后根据关键词匹配图文消息
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/queryArticle")
    public ResultData testQueryArticle(){
        ResultData resultData=new ResultData();
        Map<String,Object> condition=new HashMap<>();
        condition.put("message","云草纲目三七粉的使用");
        resultData=articleService.queryArticle(condition);
        return resultData;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/IKAnalyzer")
    public ResultData testIKAnalyzer(){
//        String text="基于java语言开发的轻量级的中文分词工具包";
//        //创建分词对象
//        Analyzer anal=new IKAnalyzer(true);
//        StringReader reader=new StringReader(text);
//        //分词
//        TokenStream ts;
//        try {
//            ts = anal.tokenStream("", reader);
//            CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);
//            //遍历分词数据
//            while(ts.incrementToken()){
//                System.out.print(term.toString()+"|");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            reader.close();
//            System.out.println();
//        }

        ResultData resultData=new ResultData();
        try {
            String str="基于java语言开发的轻量级的中文分词工具包";
            byte[] bt = str.getBytes();
            InputStream ip = new ByteArrayInputStream(bt);
            Reader read = new InputStreamReader(ip);
            IKSegmenter iks = new IKSegmenter(read,true);//true开启只能分词模式，如果不设置默认为false，也就是细粒度分割
            Lexeme t;
            while ((t = iks.next()) != null) {
                System.out.print(t.getLexemeText()+"|");
            }
        } catch (IOException e) {
            e.printStackTrace();
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }finally {
            return resultData;
        }

    }
}
