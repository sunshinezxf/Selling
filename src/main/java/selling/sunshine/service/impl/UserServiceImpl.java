package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.UserDao;
import selling.sunshine.model.User;
import selling.sunshine.service.UserService;
import selling.sunshine.utils.Encryption;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/3/16.
 */
@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public ResultData login(User user) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<String, Object>();
        user.setPassword(Encryption.md5(user.getPassword()));
        condition.put("username", user.getUsername());
        ResultData queryResponse = userDao.queryUser(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<User>) queryResponse.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                return result;
            }
            User target = ((List<User>) queryResponse.getData()).get(0);
            if (user.getUsername().equals(target.getUsername()) && user.getPassword().equals(target.getPassword())) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(target);
                return result;
            }
        }
        result.setResponseCode(queryResponse.getResponseCode());
        return result;
    }

    @Override
    public ResultData fetchUser(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = userDao.queryUser(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<User>) queryResponse.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                return result;
            }
            User target = ((List<User>) queryResponse.getData()).get(0);
            result.setData(target);
            return result;
        }
        result.setResponseCode(queryResponse.getResponseCode());
        return result;
    }


}
