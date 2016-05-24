package selling.sunshine.utils;

import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by sunshine on 5/18/16.
 */
public class PlatformConfig {

    public static String accessToken;

    private static Properties props = new Properties();

    static {
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = WechatUtil.queryAccessToken();
        }
        InputStream input = PlatformConfig.class.getClassLoader().getResourceAsStream("selling.properties");
        try {
            props.load(input);
        } catch (Exception e) {

        }
    }

    private PlatformConfig() {

    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}
