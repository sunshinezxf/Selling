package selling.sunshine.dao;

import selling.sunshine.model.Role;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 5/3/16.
 */
public interface RoleDao {
    ResultData insertRole(Role role);

    ResultData queryRole(Map<String, Object> condition);
}
