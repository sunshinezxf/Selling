package promotion.sunshine.service.impl;

import common.sunshine.model.wechat.Follower;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import promotion.sunshine.dao.FollowerDao;
import promotion.sunshine.service.FollowerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/25/16.
 */
@Service
public class FollowerServiceImpl implements FollowerService {
    private Logger logger = LoggerFactory.getLogger(FollowerServiceImpl.class);

    @Autowired
    private FollowerDao followerDao;

    @Override
    public ResultData queryFollower(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = followerDao.queryFollower(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List) response.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData subscribe(Follower follower) {
    	logger.debug("follower");
    	logger.debug(JSON.toJSONString(follower));
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("openId", follower.getOpenId());
        condition.put("channel", follower.getChannel());
        condition.put("blockFlag", false);
        ResultData response = followerDao.queryFollower(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK && !((List) response.getData()).isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("该用户已经关注过订阅号");
            return result;
        }
        response = followerDao.insertFollower(follower);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData unsubscribe(String openId) {
        logger.debug("1234567890");
        logger.debug(openId);
        ResultData result = new ResultData();
        ResultData blockResponse = followerDao.blockFollower(openId);
        result.setResponseCode(blockResponse.getResponseCode());
        result.setDescription(blockResponse.getDescription());
        return result;
    }

}
