package selling.sunshine.dao;

import selling.sunshine.model.Admin;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 4/21/16.
 */
public interface AdminDao {
    ResultData queryAdmin(Map<String, Object> condition);
    ResultData insertAdmin(Admin admin);
}
