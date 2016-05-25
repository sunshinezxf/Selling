package selling.sunshine.controller;

import com.alibaba.fastjson.JSONObject;

import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import selling.sunshine.service.FollowerService;
import selling.sunshine.utils.Encryption;
import selling.sunshine.utils.PlatformConfig;
import selling.sunshine.utils.WechatUtil;
import selling.wechat.model.Follower;
import selling.wechat.model.InMessage;
import selling.wechat.utils.XStreamFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by sunshine on 5/24/16.
 */
@RestController
public class WechatController {
    private Logger logger = LoggerFactory.getLogger(WechatController.class);

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
            InMessage message = (InMessage) content.fromXML(input);
            switch (message.getMsgType()) {
                case "event":
                    if (message.getEvent().equals("subscribe")) {
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                Follower follower = WechatUtil.queryUserInfo(message.getFromUserName(), PlatformConfig.getAccessToken());
                                followerService.subscribe(follower);
                            }
                        };
                        thread.start();
                        return "";
                    } else if (message.getEvent().equals("unsubscribe")) {
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                followerService.unsubscribe(message.getFromUserName());
                            }
                        };
                        thread.start();
                        return "";
                    }
                    break;
            }
            logger.debug(JSONObject.toJSONString(message));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "";
    }
}
