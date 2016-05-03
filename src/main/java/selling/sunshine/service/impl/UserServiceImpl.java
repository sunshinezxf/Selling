package selling.sunshine.service.impl;

import org.springframework.stereotype.Service;
import selling.sunshine.model.User;
import selling.sunshine.service.UserService;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/3/16.
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public ResultData login(User user) {
        ResultData result = new ResultData();

        return result;
    }
}
