package selling.sunshine.dao;

import selling.sunshine.model.User;
import selling.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/3/16.
 */
public interface UserDao {
    ResultData insertUser(User user);

    ResultData queryUser(Map<String, Object> condition);
    
    ResultData queryUser(Map<String, Object> condition, DataTableParam param);
}
