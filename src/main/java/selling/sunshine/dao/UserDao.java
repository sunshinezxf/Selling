package selling.sunshine.dao;

import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/3/16.
 */
public interface UserDao {
    ResultData queryUser(Map<String, Object> condition);
}
