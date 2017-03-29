package promotion.sunshine.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import common.sunshine.model.wechat.*;
import common.sunshine.utils.Encryption;
import common.sunshine.utils.ResultData;
import common.sunshine.utils.XStreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import promotion.sunshine.service.FollowerService;
import promotion.sunshine.utils.PlatformConfig;
import promotion.sunshine.utils.WechatUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by sunshine on 8/22/16.
 */
@RestController
public class WechatController {
    private Logger logger = LoggerFactory.getLogger(WechatController.class);

    /**
     * 客服的图片资源号(微信用)
     */
//    private static String KEFU1 = "Fk1CCke3zdxLUi-x3PGAxWYVEMWgEM8W8iDAjv-LV9A";
//    private static String KEFU2 = "Fk1CCke3zdxLUi-x3PGAxWkGUzd-t5-BktNRO2ZVOtE";
//    private static String KEFU3 = "Fk1CCke3zdxLUi-x3PGAxTOUCE1pVLf2dLbvr-1EfTM";
//    private static String KEFU4 = "Fk1CCke3zdxLUi-x3PGAxahgQ0VEBAKlKYoPr_9ezzQ";
//    private static String SHARE = "Fk1CCke3zdxLUi-x3PGAxR42Uic4siplrF5Z1TFQAeg";
      private static String picture = "cUgh209kI0IQhy-NqjIXRpVOWfP_j7U2XWFqg5vgH6w";

    @Autowired
    private FollowerService followerService;
//
//    @Autowired
//    private ArticleService articleService;

    /**
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/wechat")
    public String check(HttpServletRequest request) {
        String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");// 随机数
        String echostr = request.getParameter("echostr");//
        ArrayList params = new ArrayList();
        params.add(PlatformConfig.getValue("wechat_token"));
        params.add(timestamp);
        params.add(nonce);
        Collections.sort(params, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        String temp = params.get(0) + (String) params.get(1) + params.get(2);
        if (Encryption.SHA1(temp).equals(signature)) {
            return echostr;
        }
        return "";
    }
    
	/**
	 * 微信订阅号的事件配置
	 * @param request
	 * @param response
	 * @return
	 */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/wechat", produces = "text/xml;charset=utf-8")
    public String handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletInputStream stream = request.getInputStream();
            String input = WechatUtil.inputStream2String(stream);
            XStream content = XStreamFactory.init(false);
            content.alias("xml", InMessage.class);
            final InMessage message = (InMessage) content.fromXML(input);
            HttpSession session = request.getSession();
            session.setAttribute("openId", message.getFromUserName());
            logger.debug(JSONObject.toJSONString(message));
            switch (message.getMsgType()) {
            	/*
            	 * event事件
            	 */
                case "event":
                	/*
                	 * 订阅事件 
                	 */
                    if (message.getEvent().equals("subscribe")) {
                        new Thread() {
                            @Override
                            public void run() {
                                String token = PlatformConfig.getAccessToken();
                                Follower follower = WechatUtil.queryUserInfo(message.getFromUserName(), token);
                                follower.setChannel("dingyue");
                                logger.debug("imhere2");
                                ResultData rtn = followerService.subscribe(follower);
                                logger.debug(JSON.toJSONString(rtn));
                            }
                        }.start();
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = subscribe();
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    } else if (message.getEvent().equals("unsubscribe")) {
                    	/*
                    	 * 取消订阅事件
                    	 */
                        new Thread() {
                            @Override
                            public void run() {
                                followerService.unsubscribe(message.getFromUserName());
                            }
                        }.start();
                        return "";
                    } else if (message.getEvent().equalsIgnoreCase("click")) {
                    	/*
                    	 * 菜单点击事件 
                    	 */
                        if (message.getEventKey().equalsIgnoreCase("unbind")) {
                            content.alias("xml", TextOutMessage.class);
                            TextOutMessage result = new TextOutMessage();
                            result.setFromUserName(message.getToUserName());
                            result.setToUserName(message.getFromUserName());
                            result.setCreateTime(new Date().getTime());
                            result.setContent("回复'解绑'即可完成操作");
                            String xml = content.toXML(result);
                            return xml;
                        }
                        if (message.getEventKey().equalsIgnoreCase("purchase")) {
                            content.alias("xml", TextOutMessage.class);
                            TextOutMessage result = new TextOutMessage();
                            result.setFromUserName(message.getToUserName());
                            result.setToUserName(message.getFromUserName());
                            result.setCreateTime(new Date().getTime());
                            result.setContent("欢迎您对云草纲目的关注,客户自助购买即将上线,敬请期待!");
                            String xml = content.toXML(result);
                            return xml;
                        }
                        if (message.getEventKey().equalsIgnoreCase("giftEvent")) {
                            String openId = message.getFromUserName();
                            content.alias("xml", Articles.class);
                            content.alias("item", Article.class);
                            Articles result = new Articles();
                            result.setFromUserName(message.getToUserName());
                            result.setToUserName(message.getFromUserName());
                            result.setCreateTime(new Date().getTime());
                            List<Article> list = new ArrayList<>();
                            Article welcome = new Article();
                            welcome.setTitle("中秋送礼｜您给TA的礼物，我们准备");
                            welcome.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GomT0kIicicnlUhIrv1Vo9nnfkI7d2q1OyyIhB31ySyokloljpnFSOF6oMfejvt25WnPkNAia5HkQ5Og/0?wx_fmt=jpeg");
                            welcome.setUrl("http://event.yuncaogangmu.com/event/zqhd/" + openId);
                            list.add(welcome);
                            result.setArticles(list);
                            result.setArticleCount(list.size());
                            content.processAnnotations(Article.class);
                            String xml = content.toXML(result);
                            logger.debug(JSON.toJSONString(xml));
                            return xml;
                        }
                    }
                    break;
                case "text":
                	/*
                	 * 文字回复事件
                	 */
                    if (message.getContent().equals("团圆")) {
                    }
                    if (message.getContent().equals("三七") || message.getContent().equals("云草纲目")
                            || message.getContent().equals("云草") || message.getContent().equals("使用")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("三七与云草，关于三七你要知道的全部");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GqkhtCiabgDmIJnaRyWcohVlo7rZUFbZV3sUq5YoZbpaY2Jl4nhicWzGuPruJzMZ0cXKee5zJxXvcicQ/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s/WOlOHz7oQiwJc6HCfx9xLQ");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("高血压") || message.getContent().equals("高血脂") || message.getContent().equals("高脂血症") || message.getContent().equals("高血糖") || message.getContent().equals("三高")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("高血压、高血脂、高血糖");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_png/LvSutA9l9GquGha0jOh27BEXhM74IoIJ7a1TE9bEd4ZIMbstWUEYSqYvLf5RJOjY6pic0DQaIfSWwZJdcfmibGfQ/0?wx_fmt=png");
                        article.setUrl("http://mp.weixin.qq.com/s/5dU4Lm407ZUE3KEGoIexxQ");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("心脑血管疾病") || message.getContent().equals("血管") ||
                            message.getContent().equals("中风") || message.getContent().equals("冠心病") || message.getContent().equals("软化血管")
                            || message.getContent().equals("心痛") || message.getContent().equals("心肌梗死")
                            || message.getContent().equals("血管堵塞") || message.getContent().equals("血管硬化")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("心脑血管疾病");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_png/LvSutA9l9GquGha0jOh27BEXhM74IoIJ9V7jZ4EghyloeicszAa4I3aIiaPibRzicsaWzNroZXkneoq9RbZnDdp6yg/0?wx_fmt=png");
                        article.setUrl("http://mp.weixin.qq.com/s/gdlj1zeesmZge_ZObFCvBA");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("糖尿病")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("糖尿病及其并发症");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_png/LvSutA9l9GquGha0jOh27BEXhM74IoIJQUQgOsqF8suFHyMPbHU6x1gcbJccaH9bZejLhfic28Yj3d65P1ASkOA/0?wx_fmt=png");
                        article.setUrl("http://mp.weixin.qq.com/s/iHA2PwnKnxYF6dMIsuIFNA");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("脂肪肝") || message.getContent().equals("酒精肝") || message.getContent().equals("肝")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("肝脏有问题，三七帮您治");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_png/LvSutA9l9GquGha0jOh27BEXhM74IoIJib7nibUC6IAFcrTcLf9PeicMNIsWgWSWFxlDCBVpfrnkEljSKeMC63lVQ/0?wx_fmt=png");
                        article.setUrl("http://mp.weixin.qq.com/s/U-FfskIWr-WqHxqTpshlsg");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("癌症") || message.getContent().equals("肿瘤")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("笑对肿瘤，干掉癌症");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GqkhtCiabgDmIJnaRyWcohVlNs7ZAVNCwibMBm11WpicSekJ1tvoyxPJUH8xpUiaOjMsPyxUCV8Fy4k3g/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s/Mt82rRgeHhPzMF079X2gGw");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("补血") || message.getContent().equals("贫血") || message.getContent().equals("血虚")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("补血");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_png/LvSutA9l9GquGha0jOh27BEXhM74IoIJHxUnMpA4k4IpxPjq0lSyibgCgDdj8HVGfRjzy3ibUCoht9LBxH0Mmiakw/0?wx_fmt=png");
                        article.setUrl("http://mp.weixin.qq.com/s/WbR0iM_KpEYEr9i9oIxyOA");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("女性") || message.getContent().equals("祛斑") || message.getContent().equals("美白") || message.getContent().equals("冻龄") || message.getContent().equals("养颜")
                            || message.getContent().equals("护肤") || message.getContent().equals("补水") || message.getContent().equals("保湿") || message.getContent().equals("防晒")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("女性，美丽健康永相伴");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GquGha0jOh27BEXhM74IoIJW1zE4uSSehtTyib3PMVVAT4XcuInBaJcNOQJbzGkKnrsKEeIc2eJ6cw/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s/5uA8yaSN83duXZoacxKK7w");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("美食") || message.getContent().equals("做饭")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("赏食·本周吃什么");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GquGha0jOh27BEXhM74IoIJIFLubTgHKD9w9B2PibMBS8FJos2jPZic4iaqJ0ppW92dIuiaiblGoHIYlgw/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s/GrGzk1fYWX5CmtjX3UAa7g");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("减肥") || message.getContent().equals("瘦身") || message.getContent().equals("睡眠") || message.getContent().equals("失眠") || message.getContent().equals("精力不济")
                            || message.getContent().equals("腰疼") || message.getContent().equals("酸") || message.getContent().equals("肠胃") || message.getContent().equals("头疼") || message.getContent().equals("感冒")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("告别现代病");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GquGha0jOh27BEXhM74IoIJOGjUjK97EAvo3IIbheZCVmuqRwY7EdDMYBsjcibOias2MbXNX96fYQnA/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s/jnTTIQjyRUBjNPdMhrvq_w");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("舌") || message.getContent().equals("体质") || message.getContent().equals("血瘀")
                            || message.getContent().equals("痰湿") || message.getContent().equals("气虚") || message.getContent().equals("湿气")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("看舌知百病");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_png/LvSutA9l9GquGha0jOh27BEXhM74IoIJ069GVFsmcggygKoIK5E3zic6B0Ez7WOy6WzM1Hliamy7tZvXaqLiaol1A/0?wx_fmt=png");
                        article.setUrl("http://mp.weixin.qq.com/s/PU0ujGlc4TM93L6996vA1w");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("四季") || message.getContent().equals("养生") || message.getContent().equals("节气")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("四季与养生");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GquGha0jOh27BEXhM74IoIJhnpTVAFqgGe2gRE5klmqq6h6JcHqhibG6f3S4luez007j7hP5U7pBrA/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s/pMbWBXmmXPpo186EEGiQIQ");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("陈淑长")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("真实案例：70岁老教授年轻20岁的秘密是... | 云草与三七");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GqmdeibHMNA2kr8DleCBGEnzvgn5eH8ia69Cs1GY5VQYblfTPpc3RPQafoP5m5PrLAEZAoiaTUibGQScA/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247485499&idx=1&sn=a56bf963166f0aee095249f1b48efb6f&chksm=ea7be413dd0c6d050b3c65d7c204100f797a2198daf512c0e8ddccbce93ef7b46d0d7db3b48f&scene=21#wechat_redirect");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("面膜")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("与其花千万，不如DIY：三七面膜，美白一夏｜永远二十岁");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GqmdeibHMNA2kr8DleCBGEnzvgn5eH8ia69Cs1GY5VQYblfTPpc3RPQafoP5m5PrLAEZAoiaTUibGQScA/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247483688&idx=1&sn=ce6866ffd5b0637b67b08bce4253eda7&scene=21#wechat_redirect");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("青春期功血") || message.getContent().equals("青春期宫血")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("三七治好了我女儿的月经不调和青春期功血！| 三七与云草");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GqmdeibHMNA2kr8DleCBGEnzvgn5eH8ia69Cs1GY5VQYblfTPpc3RPQafoP5m5PrLAEZAoiaTUibGQScA/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247484682&idx=1&sn=2bd3e7b57a378828212511f27e14910e&chksm=ea7be922dd0c6034b18879d9eddb82dbabfca2e2b3cd7d0bf8f3fec3cdbae37043bd49af0afb&scene=21#wechat_redirect");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("养生堂")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("不再暴殄天物，三七三大功能熟记于心，百病除｜三七与云草");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GqmdeibHMNA2kr8DleCBGEnzvgn5eH8ia69Cs1GY5VQYblfTPpc3RPQafoP5m5PrLAEZAoiaTUibGQScA/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247483750&idx=1&sn=a469fa10ec978b80ffa4cb80a5364327&scene=21#wechat_redirect");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("健康之路")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("三七真的那么神？看看《健康之路》专家怎么说 | 三七与云草");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GqmdeibHMNA2kr8DleCBGEnzvgn5eH8ia69Cs1GY5VQYblfTPpc3RPQafoP5m5PrLAEZAoiaTUibGQScA/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247484971&idx=1&sn=af829b4c85566aba02b99c14a8762abd&chksm=ea7bea03dd0c63151c050909faa3bd9cc03eb3fb1747f51f39f8583e6016b2db8e91f08835ca&scene=21#wechat_redirect");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("雾霾")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("空气指数再爆表，三七还能抗雾霾？| 三七与云草");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GqmdeibHMNA2kr8DleCBGEnzvgn5eH8ia69Cs1GY5VQYblfTPpc3RPQafoP5m5PrLAEZAoiaTUibGQScA/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247485920&idx=1&sn=507cd055e115dd3400780f09e2030125&chksm=ea7be5c8dd0c6cde3a527d91e59701502da1cf6a7c6dac1c9c45978ac7493e9c4011c04163c7&scene=21#wechat_redirect");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().equals("查询") || message.getContent().equals("快递") || message.getContent().equals("物流") || message.getContent().equals("订单")) {
                        content.alias("xml", Articles.class);
                        content.alias("item", Article.class);
                        Articles result = new Articles();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        List<Article> list = new ArrayList<>();
                        Article article = new Article();
                        article.setTitle("如何自助查询订单物流？| 公众号使用指南");
                        article.setPicUrl("https://mmbiz.qlogo.cn/mmbiz_jpg/LvSutA9l9GqmdeibHMNA2kr8DleCBGEnzvgn5eH8ia69Cs1GY5VQYblfTPpc3RPQafoP5m5PrLAEZAoiaTUibGQScA/0?wx_fmt=jpeg");
                        article.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247484266&idx=3&sn=0d3c9e5b6c3f54960146625387d8759b&chksm=ea7bef42dd0c66546dc3897691eaf77056558f893bff0098c6430c8d8d88b840f8ce699738f2&scene=21#wechat_redirect");
                        list.add(article);
                        result.setArticles(list);
                        result.setArticleCount(list.size());
                        content.processAnnotations(Article.class);
                        String xml = content.toXML(result);
                        logger.debug(JSON.toJSONString(xml));
                        return xml;
                    }
                    if (message.getContent().contains("购买")) {
                        String openId = message.getFromUserName();
                        content.alias("xml", TextOutMessage.class);
                        TextOutMessage result = new TextOutMessage();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        result.setContent("您好。如果您想购买云草产品，请扫码添加我们的健康大使微信。他将帮助您完成购买，还有优惠价格哦！");
                        String xml = content.toXML(result);
                        String token = WechatUtil.queryAccessToken();
                        WechatUtil.sendImageMessage(token, openId, picture);
                        return xml;
                    }

                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "";
    }

    /**
     * 订阅事件触发后，订阅号推送文章
     * @return
     */
    private List<Article> subscribe() {
        List<Article> list = new ArrayList<>();
        Article welcome = new Article();
        welcome.setTitle("我们的故事｜关于家人关于爱");
        welcome.setDescription("云草纲目，每一瓶，都是家人与朋友满满的爱。");
        welcome.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/zhe7KjM5iaS8Z1VmBFxR793iaJhia9fKCkz0BibJy4bWnLrhLlWHVAqibXGZQz1KiaqWBg6Ikzw7Mbs97EHq1bO6uZibw/0?wx_fmt=jpeg");
        welcome.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=100000002&idx=1&sn=3fd71feb926b721f1b2473f70f76f85a#rd");
        list.add(welcome);
        Article effect = new Article();
        effect.setTitle("千金不换｜三七功效，看这一篇就够啦！");
        effect.setDescription("看似矛盾的两种功能——止血与活血，竟然在三七上完美呈现，这是中药的神奇之处。三七这种看似矛盾的和谐，正是中国文化的核心，也是中药的核心。《中庸》说道，“致中和，天地位焉，万物育焉。血气不和，百病生。”三七是和血的良药。血气和，方能健康长寿。");
        effect.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/zhe7KjM5iaS8h1soIe4bdE2y2aYpuTXQMjDycicIFo8AfCWVCdXrMk2MQlct4MmPOicKneBVfIz3rPhaItK3iapMzQ/0?wx_fmt=jpeg");
        effect.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=100000002&idx=2&sn=6ea6d17d26798283c12b7abddbcf8d21#rd");
        list.add(effect);
        Article product = new Article();
        product.setTitle("何为三七｜参中之王，千金不换");
        product.setDescription("现代研究发现，三七的化学成分和药理作用与人参相似，但其治疗外伤和心血管病的功能则是人参无法比拟的。因为人们对三七的药效的了解比人参晚了1000多年；所以，三七的名气没有人参那么大，但是他却是名副其实的参中之王。");
        product.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/zhe7KjM5iaS8HCZAY1KWib9AHYx7fscNkiaic5ZJDKB1jY1xEmFxzCAlKvEDaSdwwCCLuv2EaE2SjDm8LDhU23a74Q/0?wx_fmt=jpeg");
        product.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=100000002&idx=3&sn=06b800b2272b0da17b57a35ba880a3b5#rd");
        list.add(product);
        Article manufact = new Article();
        manufact.setTitle("制作工艺｜一颗三七的蜕变");
        manufact.setDescription("历经波澜，粉身碎骨，只为以最棒的姿态来到您的面前，每一粒三七粉都是满满的深情切意。");
        manufact.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/zhe7KjM5iaS8Z1VmBFxR793iaJhia9fKCkzOXSogNWy72UyffanIVxDrWYNibWibVGODmpacwhCWRHBZTALzMfDCHpg/0?wx_fmt=png");
        manufact.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=100000002&idx=4&sn=2142332eba0a6764c41b380b17d0b06d#rd");
        list.add(manufact);
        Article guidance = new Article();
        guidance.setTitle("使用指南｜三七那么好，该怎么物尽其用？");
        guidance.setDescription("三七作为一种具有活血化瘀、止血定痛等多种药效的中药，功效广为人知。但并不是每个人都适合服用三七，也不是服用的越多效果就一定好，服用不当反而会伤害身体。炎炎夏日，来份三七美食，既养生又好吃～");
        guidance.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/LvSutA9l9GqJsJ1bnfj330qsRziaECfFPyB5A9Y9XpiaTaFwZucAJwLEBAqXe62jxOMAATYDRrIASz7gBRhymcNQ/0?wx_fmt=jpeg");
        guidance.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=100000002&idx=5&sn=f25e272574d1c10549d2f513b31f0361#rd");
        list.add(guidance);
        Article eye = new Article();
        eye.setTitle("火眼金睛｜市场乱象中，如何辨别好的三七？");
        eye.setDescription("今年2月，市民范女士反映：“我看电视里说常吃三七粉对身体好，，就去药店花了600多元买了一斤。回家一看，发现粉质粗糙，像沙子一样，而且喝起来也不苦，只有木屑似的味道。我怀疑自己买的是假三七粉，但又不能确定。我就想问问，三七粉如何辨别真伪？”");
        eye.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/LvSutA9l9GqJsJ1bnfj330qsRziaECfFPBiabSpVd49CbpyeGomAicTctcicpmnkr2kkibe9PibJzW5LL2xB7FXLibSYA/0?wx_fmt=jpeg");
        eye.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=100000002&idx=6&sn=03566bbbcd37522c0c1aebee3add30cc#rd");
        list.add(eye);
        return list;
    }
}
