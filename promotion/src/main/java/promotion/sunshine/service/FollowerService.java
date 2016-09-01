package promotion.sunshine.service;

import java.util.Map;

import common.sunshine.model.wechat.Follower;
import common.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/25/16.
 */
public interface FollowerService {
    ResultData queryFollower(Map<String, Object> condition);

    ResultData subscribe(Follower follower);

    ResultData unsubscribe(String openId);
    
}
