package selling.sunshine.service;

import common.sunshine.utils.ResultData;
import selling.wechat.model.Follower;

/**
 * Created by sunshine on 5/25/16.
 */
public interface FollowerService {
    ResultData subscribe(Follower follower);

    ResultData unsubscribe(String openId);
}
