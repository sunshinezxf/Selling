package selling.sunshine.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selling.sunshine.utils.PlatformConfig;
import selling.sunshine.utils.WechatUtil;

/**
 * Created by sunshine on 5/24/16.
 */
public class WechatSchedule {
    private Logger logger = LoggerFactory.getLogger(WechatSchedule.class);

    public void schedule() {
        PlatformConfig.accessToken = WechatUtil.queryAccessToken();
    }
}
