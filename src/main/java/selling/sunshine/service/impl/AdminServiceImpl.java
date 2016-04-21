package selling.sunshine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.AdminDao;
import selling.sunshine.model.Admin;
import selling.sunshine.service.AdminService;
import selling.sunshine.utils.Encryption;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/17/16.
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Override
    public ResultData login(Admin admin) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<String, Object>();
        admin.setPassword(Encryption.md5(admin.getPassword()));
        condition.put("username", admin.getUsername());
        ResultData queryResponse = adminDao.queryAdmin(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Admin>) queryResponse.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                return result;
            }
            Admin target = ((List<Admin>) queryResponse.getData()).get(0);
            if (admin.getUsername().equals(target.getUsername()) && admin.getPassword().equals(admin.getPassword())) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(target);
                return result;
            }
        }
        result.setResponseCode(ResponseCode.RESPONSE_NULL);
        return result;
    }
}
