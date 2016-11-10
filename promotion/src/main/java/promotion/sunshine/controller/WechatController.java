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

    //    private static String KEFU1 = "ytSMk-WMouat73cGNz5jwBVCCs6hDPl071GteBgYa32oYqAw6lT9yMedTFzoeQx6";
//    private static String KEFU2 = "aP2fkurURlOPGxaL-4MNdkTZLK41i_676GjdaaeWi8dz95RzS8cHo6ZACo35Qz1D";
//    private static String KEFU3 = "nNre-oi78h1iTmRfSwNJcTr5W0Lw9VkNXzLCZu8P6DRO71J_D6dsKLr_5OWvH78k";
//    private static String KEFU4 = "Eca1cAZ0-cryjo8hI5CTGKxyupPYFJFTd8PYZaWBu1AceJHvDpYhmugsBmqFDUgs";
    private static String KEFU1 = "Fk1CCke3zdxLUi-x3PGAxWYVEMWgEM8W8iDAjv-LV9A";
    private static String KEFU2 = "Fk1CCke3zdxLUi-x3PGAxWkGUzd-t5-BktNRO2ZVOtE";
    private static String KEFU3 = "Fk1CCke3zdxLUi-x3PGAxTOUCE1pVLf2dLbvr-1EfTM";
    private static String KEFU4 = "Fk1CCke3zdxLUi-x3PGAxahgQ0VEBAKlKYoPr_9ezzQ";
    private static String SHARE = "Fk1CCke3zdxLUi-x3PGAxR42Uic4siplrF5Z1TFQAeg";

    @Autowired
    private FollowerService followerService;

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
                case "event":
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
                        new Thread() {
                            @Override
                            public void run() {
                                followerService.unsubscribe(message.getFromUserName());
                            }
                        }.start();
                        return "";
                    } else if (message.getEvent().equalsIgnoreCase("click")) {
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
                    if (message.getContent().equals("团圆")) {
                        /*
                        String openId = message.getFromUserName();
                        content.alias("xml", TextOutMessage.class);
                        TextOutMessage result = new TextOutMessage();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        result.setContent("1. 点击下方菜单栏“活动”中的“中秋活动”，申请页面自动弹出。填写完毕后请按右上角转发至朋友圈（如图1），将好消息分享给更多亲朋～\n2. 扫码添加健康大使（图2），将第1步您的转发截图发送给TA，并告诉TA您最早是从哪里知道我们的活动消息～\n填链接，转票圈；加大使，发截图；搞定！如果您有任何问题，都可以咨询健康大使，TA会为您提供1对1的定制健康服务哦～");
                        String xml = content.toXML(result);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String thirteen = "13:00:00";
                        String seventeen = "17:00:00";
                        String twenty = "20:00:00";
                        String twentythree = "23:00:00";
                        String now = (new SimpleDateFormat("HH:mm:ss")).format(new Date());
                        String media_id = null;
                        if (now.compareTo(twentythree) >= 0 || thirteen.compareTo(now) >= 0) {
                            media_id = KEFU1;
                        } else if (now.compareTo(thirteen) >= 0 && seventeen.compareTo(now) >= 0) {
                            media_id = KEFU2;
                        } else if (now.compareTo(seventeen) >= 0 && twenty.compareTo(now) >= 0) {
                            media_id = KEFU3;
                        } else {
                            media_id = KEFU4;
                        }
                        String token = WechatUtil.queryAccessToken();
                        WechatUtil.sendImageMessage(token, openId, SHARE);
                        WechatUtil.sendImageMessage(token, openId, media_id);
                        return xml;
                        */
                    }
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "";
    }

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
