package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.FollowerDao;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import selling.wechat.model.Follower;

/**
 * Created by sunshine on 5/25/16.
 */
@Repository
public class FollowerDaoImpl extends BaseDao implements FollowerDao {

    private Logger logger = LoggerFactory.getLogger(FollowerDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertFollower(Follower follower) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.insert("selling.wechat.insert", follower);
                result.setData(follower);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }
}
