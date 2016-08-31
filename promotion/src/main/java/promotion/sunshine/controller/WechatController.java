package promotion.sunshine.controller;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import common.sunshine.model.wechat.InMessage;
import common.sunshine.utils.Encryption;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import common.sunshine.utils.XStreamFactory;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
import common.sunshine.model.wechat.Article;
import common.sunshine.model.wechat.Articles;
import common.sunshine.model.wechat.Follower;
import common.sunshine.model.wechat.TextOutMessage;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
            switch (message.getMsgType()) {
                case "event":
                    if (message.getEvent().equals("subscribe")) {
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                Follower follower = WechatUtil.queryUserInfo(message.getFromUserName(), PlatformConfig.getAccessToken());
                                follower.setChannel("dingyue");
                                logger.debug("imhere2");
                                followerService.subscribe(follower);
                            }
                        };
                        thread.start();
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
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                followerService.unsubscribe(message.getFromUserName());
                            }
                        };
                        thread.start();
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
                        if(message.getEventKey().equalsIgnoreCase("giftEvent")) {
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
                		String openId = message.getFromUserName();
                		content.alias("xml", TextOutMessage.class);
                    	TextOutMessage result = new TextOutMessage();
                        result.setFromUserName(message.getToUserName());
                        result.setToUserName(message.getFromUserName());
                        result.setCreateTime(new Date().getTime());
                        result.setContent("1. 请点击下方菜单栏“中秋活动”，填写领取申请。填写完毕后请按右上角转发至朋友圈（如图1），将您的好消息分享给更多亲朋～\n2. 扫码添加健康大使微信（图2），将第1步的转发截图，以及您的信息获取来源截图给健康大使（比如您在谁的朋友圈看到活动信息，就将他的那条朋友圈分享截图；如果直接是公众号获取，就发公众号截图）。如果您有任何问题，都可以咨询这位健康大使，TA会为您提供1对1的定制健康服务哦～");
                        String xml = content.toXML(result);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String thirteen = "13:00:00";
                        String seventeen = "17:00:00";
                        String twenty = "20:00:00";
                        String twentythree = "23:00:00";
                        String now = (new SimpleDateFormat("HH:mm:ss")).format(new Date());
                        String media_id = null;
                        if(now.compareTo(twentythree) >= 0 || thirteen.compareTo(now) >= 0) {
                        	media_id = KEFU1;
                        } else if(now.compareTo(thirteen) >= 0 && seventeen.compareTo(now) >= 0) {
                        	media_id = KEFU2;
                        } else if(now.compareTo(seventeen) >=0 && twenty.compareTo(now) >= 0) {
                        	media_id = KEFU3;
                        } else {
                        	media_id = KEFU4;
                        }
                        WechatUtil.sendImageMessage(WechatUtil.queryAccessToken(), openId, media_id);
                        return xml;
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
        welcome.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247483666&idx=1&sn=84bdafae0d39c1c9d4c1b85bb8bf184a");
        list.add(welcome);
        Article guidance = new Article();
        guidance.setTitle("使用指南｜云草纲目代理商管理手册");
        guidance.setDescription("欢迎加入“云草纲目”大家庭，我们共同努力把最信任的佳品分享给身边最在乎的人。");
        guidance.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/zhe7KjM5iaSibkQicWl47EG0pdFrtLBx96445SZ2qdVOLcl0Lbjn0SCibGO0MFHiccRaoniawAaM8JBmPdIiavbFKmRNA/0?wx_fmt=jpeg");
        guidance.setUrl("http://mp.weixin.qq.com/s?__biz=MzIwNjI1OTY2Mg==&mid=503177455&idx=1&sn=ad4c39e7083a36e89009e4f72ef4f139");
        list.add(guidance);
        Article product = new Article();
        product.setTitle("何为三七｜参中之王，千金不换");
        product.setDescription("现代研究发现，三七的化学成分和药理作用与人参相似，但其治疗外伤和心血管病的功能则是人参无法比拟的。因为人们对三七的药效的了解比人参晚了1000多年；所以，三七的名气没有人参那么大，但是他却是名副其实的参中之王。");
        product.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/zhe7KjM5iaS8HCZAY1KWib9AHYx7fscNkiaic5ZJDKB1jY1xEmFxzCAlKvEDaSdwwCCLuv2EaE2SjDm8LDhU23a74Q/0?wx_fmt=jpeg");
        product.setUrl("http://mp.weixin.qq.com/s?__biz=MzIwNjI1OTY2Mg==&mid=2650661098&idx=1&sn=74b3f4690b182c295ba8b8ffc1e65b38");
        list.add(product);
        Article effect = new Article();
        effect.setTitle("参中之王｜三七功效，看这一篇就够啦！");
        effect.setDescription("看似矛盾的两种功能——止血与活血，竟然在三七上完美呈现，这是中药的神奇之处。三七这种看似矛盾的和谐，正是中国文化的核心，也是中药的核心。《中庸》说道，“致中和，天地位焉，万物育焉。血气不和，百病生。”三七是和血的良药。血气和，方能健康长寿。");
        effect.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/zhe7KjM5iaS8h1soIe4bdE2y2aYpuTXQMjDycicIFo8AfCWVCdXrMk2MQlct4MmPOicKneBVfIz3rPhaItK3iapMzQ/0?wx_fmt=jpeg");
        effect.setUrl("https://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247483670&idx=1&sn=ff34c12e664c54bcb6a9135d8cb6a54c");
        list.add(effect);
        Article manufact = new Article();
        manufact.setTitle("制作工艺｜一颗三七的蜕变");
        manufact.setDescription("历经波澜，粉身碎骨，只为以最棒的姿态来到您的面前，每一粒三七粉都是满满的深情切意。");
        manufact.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/zhe7KjM5iaS8Z1VmBFxR793iaJhia9fKCkzOXSogNWy72UyffanIVxDrWYNibWibVGODmpacwhCWRHBZTALzMfDCHpg/0?wx_fmt=png");
        manufact.setUrl("http://mp.weixin.qq.com/s?__biz=MzI1OTMyNTI1NQ==&mid=2247483673&idx=1&sn=2f5f641373d96503219dae12eda7f4d4");
        list.add(manufact);
        return list;
    }
}
