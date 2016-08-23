package promotion.sunshine.utils;

import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by sunshine on 5/18/16.
 */
public class PlatformConfig {

    private static String accessToken;

    private static String jsapiTicket;

    private static Properties props = new Properties();

    static {
        InputStream input = PlatformConfig.class.getClassLoader().getResourceAsStream("promotion.properties");
        try {
            props.load(input);
        } catch (Exception e) {

        }
    }

    private PlatformConfig() {

    }

    public static String getAccessToken() {
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = WechatUtil.queryAccessToken();
        }
        return accessToken;
    }

    public static String getJsapiTicket() {
        if (StringUtils.isEmpty(jsapiTicket)) {
            getAccessToken();
        }
        return jsapiTicket;
    }

    public static void setJsapiTicket(String jsapiTicket) {
        PlatformConfig.jsapiTicket = jsapiTicket;
    }

    public static void setAccessToken(String token) {
        accessToken = token;
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}
