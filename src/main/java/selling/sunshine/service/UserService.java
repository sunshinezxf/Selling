package selling.sunshine.service;

import selling.sunshine.model.User;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/3/16.
 */
public interface UserService {
    ResultData login(User user);
}
