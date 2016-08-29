package promotion.sunshine.dao;

import common.sunshine.model.wechat.Follower;
import common.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/25/16.
 */
public interface FollowerDao {
    ResultData insertFollower(Follower follower);

    ResultData blockFollower(String openId);
}
