package selling.sunshine.dao;

import selling.sunshine.model.Admin;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 4/21/16.
 */
public interface AdminDao {
    ResultData queryAdmin(Map<String, Object> condition);

    ResultData queryAdminByPage(Map<String, Object> condition, DataTableParam param);

    ResultData insertAdmin(Admin admin);

    ResultData updateAdmin(Admin admin);

    ResultData deleteAdmin(Admin admin);
}
