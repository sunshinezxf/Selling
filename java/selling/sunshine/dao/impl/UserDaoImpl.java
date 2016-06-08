package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.UserDao;
import selling.sunshine.model.User;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/3/16.
 */
@Repository
public class UserDaoImpl extends BaseDao implements UserDao {
    private Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private Object lock = new Object();

    @Transactional
    @Override
    public ResultData insertUser(User user) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.insert("selling.user.insert", user);
                if (user.getAdmin() != null) {
                    sqlSession.insert("selling.admin.insert", user.getAdmin());
                }
                if (user.getAgent() != null) {
                    sqlSession.insert("selling.agent.insert", user.getAgent());
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    @Override
    public ResultData queryUser(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<User> list = sqlSession.selectList("selling.user.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }


}
