package selling.sunshine.dao;

import selling.sunshine.model.express.Express4Agent;
import selling.sunshine.model.express.Express4Customer;
import common.sunshine.utils.ResultData;

import java.util.Map;

public interface ExpressDao {
    ResultData insertExpress4Agent(Express4Agent express);

    ResultData insertExpress4Customer(Express4Customer express);

    ResultData queryExpress(Map<String, Object> condition);

    ResultData queryExpress4Agent(Map<String, Object> condition);

    ResultData queryExpress4Customer(Map<String, Object> condition);

}
