package selling.sunshine.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by sunshine on 5/18/16.
 */
public class PlatformConfig {
    private static Properties props = new Properties();

    static {
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
