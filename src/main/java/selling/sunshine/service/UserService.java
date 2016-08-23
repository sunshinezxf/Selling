package selling.sunshine.service;

import common.sunshine.model.selling.user.User;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/3/16.
 */
public interface UserService {
    ResultData login(User user);

    ResultData fetchUser(Map<String, Object> condition);
    
    ResultData fetchUser(Map<String, Object> condition, DataTableParam param);
}
