package selling.sunshine.service.impl;

import common.sunshine.utils.Encryption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.AdminDao;
import selling.sunshine.model.Admin;
import selling.sunshine.model.Role;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AdminService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/17/16.
 */
@Service
public class AdminServiceImpl implements AdminService {
    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

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
            if (admin.getUsername().equals(target.getUsername())
                    && admin.getPassword().equals(admin.getPassword())) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(target);
                return result;
            }
        }
        result.setResponseCode(ResponseCode.RESPONSE_NULL);
        return result;
    }

    @Override
    public ResultData createAdmin(Admin admin, Role role) {
        ResultData result = new ResultData();
        admin.setPassword(Encryption.md5(admin.getPassword()));
        ResultData insertResponse = adminDao.insertAdmin(admin, role);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchAdmin(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = adminDao.queryAdmin(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchAdmin(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = adminDao.queryAdminByPage(condition, param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List) queryResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData updateAdmin(Admin admin) {
        ResultData result = new ResultData();
        admin.setPassword(Encryption.md5(admin.getPassword()));
        ResultData updateResponse = adminDao.updateAdmin(admin);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData deleteAdmin(Admin admin) {
        ResultData result = new ResultData();
        ResultData deleteResponse = adminDao.deleteAdmin(admin);
        result.setResponseCode(deleteResponse.getResponseCode());
        if (deleteResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(deleteResponse.getData());
        } else {
            result.setDescription(deleteResponse.getDescription());
        }
        return result;
    }
}
