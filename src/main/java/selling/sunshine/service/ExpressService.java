package selling.sunshine.service;

import selling.sunshine.model.express.Express4Agent;
import selling.sunshine.model.express.Express4Customer;
import selling.sunshine.utils.ResultData;

import java.util.Map;

public interface ExpressService {

    ResultData createExpress(Express4Agent express);

    ResultData createExpress(Express4Customer express);

    ResultData fetchExpress(Map<String, Object> condition);

    ResultData fetchExpress4Agent(Map<String, Object> condition);

    ResultData fetchExpress4Customer(Map<String, Object> condition);

    ResultData traceExpress(String expressNo, String type);
}
