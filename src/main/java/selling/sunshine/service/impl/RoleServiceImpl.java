package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.RoleDao;
import selling.sunshine.service.RoleService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 7/5/16.
 */
@Service
public class RoleServiceImpl implements RoleService {

    private Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleDao roleDao;

    @Override
    public ResultData queryRole(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData fetchResponse = roleDao.queryRole(condition);
        result.setResponseCode(fetchResponse.getResponseCode());
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List) fetchResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(fetchResponse.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }
}
