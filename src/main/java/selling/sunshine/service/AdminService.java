package selling.sunshine.service;

import selling.sunshine.model.Admin;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 4/17/16.
 */
public interface AdminService {
    ResultData login(Admin admin);
    ResultData createAdmin(Admin admin);
}
