package selling.sunshine.dao.impl;

import org.springframework.stereotype.Repository;
import selling.sunshine.dao.UserDao;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/3/16.
 */
@Repository
public class UserDaoImpl implements UserDao {
    @Override
    public ResultData queryUser(Map<String, Object> condition) {
        ResultData result = new ResultData();

        return result;
    }
}
