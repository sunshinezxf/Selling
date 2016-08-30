package promotion.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.sunshine.model.wechat.Follower;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import promotion.sunshine.dao.FollowerDao;
import promotion.sunshine.service.FollowerService;

/**
 * Created by sunshine on 5/25/16.
 */
@Service
public class FollowerServiceImpl implements FollowerService {
    private Logger logger = LoggerFactory.getLogger(FollowerServiceImpl.class);

    @Autowired
    private FollowerDao followerDao;

    @Override
    public ResultData subscribe(Follower follower) {
        ResultData result = new ResultData();
        ResultData insertResponse = followerDao.insertFollower(follower);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData unsubscribe(String openId) {
        ResultData result = new ResultData();
        ResultData blockResponse = followerDao.blockFollower(openId);
        result.setResponseCode(blockResponse.getResponseCode());
        result.setDescription(blockResponse.getDescription());
        return result;
    }
}
