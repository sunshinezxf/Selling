package selling.sunshine.service.impl;

import org.springframework.stereotype.Service;
import selling.sunshine.model.Admin;
import selling.sunshine.service.AdminService;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 4/17/16.
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Override
    public ResultData login(Admin admin) {
        ResultData result = new ResultData();

        return result;
    }
}
