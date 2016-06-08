package selling.sunshine.service;

import java.util.Map;

import selling.sunshine.model.Admin;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 4/17/16.
 */
public interface AdminService {
    ResultData login(Admin admin);
    ResultData createAdmin(Admin admin);
    ResultData fetchAdmin(Map<String, Object> condition);
    ResultData updateAdmin(Admin admin);
    ResultData deleteAdmin(Admin admin);
}
