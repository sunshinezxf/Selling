package promotion.sunshine.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import promotion.sunshine.utils.PlatformConfig;
import promotion.sunshine.utils.WechatUtil;

/**
 * Created by sunshine on 5/24/16.
 */
public class WechatSchedule {
    private Logger logger = LoggerFactory.getLogger(WechatSchedule.class);

    public void schedule() {
        String token = WechatUtil.queryAccessToken();
        PlatformConfig.setAccessToken(token);
        logger.info("[Wechat Token]: " + token);
    }
}
