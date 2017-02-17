package promotion.sunshine.service;

import java.util.Map;

import common.sunshine.model.wechat.Follower;
import common.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/25/16.
 */
public interface FollowerService {
	//查询订阅者
    ResultData queryFollower(Map<String, Object> condition);

    //订阅事件
    ResultData subscribe(Follower follower);

    //取关事件
    ResultData unsubscribe(String openId);
    
}
