package promotion.sunshine.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

/**
 * Created by sunshine on 5/27/16.
 */
public class WechatConfig {
    private static Logger logger = LoggerFactory.getLogger(WechatConfig.class);

    public static Configuration config(String url) {
        Configuration result = new Configuration();
        String nonceStr = createNonceStr();
        String timestamp = createTimestamp();
        String signature = "";
        String jsapiTicket = getJsapiTicket();
        StringBuffer sb = new StringBuffer();
        sb.append("jsapi_ticket=").append(jsapiTicket).append("&noncestr=")
                .append(nonceStr).append("&timestamp=").append(timestamp).append("&url=").append(url);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sb.toString().getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result.setJsapiTicket(jsapiTicket);
        result.setNonceStr(nonceStr);
        result.setSignature(signature);
        result.setTimestamp(timestamp);
        result.setUrl(url);
        return result;
    }

    public static void oauthWechat(ModelAndView view, String urlBase) {
        String url = "http://" + PlatformConfig.getValue("server_url") + urlBase;
        String configUrl = url + "";
        Configuration configuration = WechatConfig.config(configUrl);
        configuration.setShareLink(url);
        view.addObject("configuration", configuration);
    }

    public static void oauthWechat(ModelAndView view, String urlBase, String shareURL) {
        String url = "http://" + PlatformConfig.getValue("server_url") + urlBase;
        String configUrl = url + "";
        Configuration configuration = WechatConfig.config(configUrl);
        configuration.setShareLink(shareURL);
        view.addObject("configuration", configuration);
    }

    public static void oauthWechat(ModelAndView view, String urlBase, String code, String state) {
        String url = "http://" + PlatformConfig.getValue("server_url") + urlBase;
        String configUrl;
        if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(code)) {
            String openId = WechatUtil.queryOauthOpenId(code);
            configUrl = url + "?code=" + code + "&state=" + state;
            view.addObject("wechat", openId);
        } else {
            configUrl = url + "";
        }
        Configuration configuration = WechatConfig.config(configUrl);
        configuration.setShareLink(url);
        view.addObject("configuration", configuration);
    }

    private static String getJsapiTicket() {
        return PlatformConfig.getJsapiTicket();
    }

    private static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
