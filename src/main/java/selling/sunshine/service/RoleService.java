package selling.sunshine.service;

import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 7/5/16.
 */
public interface RoleService {
    ResultData queryRole(Map<String, Object> condition);
}
