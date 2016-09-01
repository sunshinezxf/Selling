package promotion.sunshine.dao;

import common.sunshine.model.wechat.Follower;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/25/16.
 */
public interface FollowerDao {
    ResultData queryFollower(Map<String, Object> condition);

    ResultData insertFollower(Follower follower);

    ResultData blockFollower(String openId);
}
