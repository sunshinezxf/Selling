package promotion.sunshine.dao;

import common.sunshine.model.wechat.Follower;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/25/16.
 */
public interface FollowerDao {
	
	//查询订阅者
    ResultData queryFollower(Map<String, Object> condition);

    //插入订阅者
    ResultData insertFollower(Follower follower);

    //把订阅者block掉
    ResultData blockFollower(String openId);
}
