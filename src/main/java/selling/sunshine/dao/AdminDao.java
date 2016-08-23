package selling.sunshine.dao;

import common.sunshine.model.selling.admin.Admin;
import common.sunshine.model.selling.user.Role;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 4/21/16.
 */
public interface AdminDao {
    ResultData queryAdmin(Map<String, Object> condition);

    ResultData queryAdminByPage(Map<String, Object> condition, DataTableParam param);

    ResultData insertAdmin(Admin admin, Role role);

    ResultData updateAdmin(Admin admin);

    ResultData deleteAdmin(Admin admin);
}
