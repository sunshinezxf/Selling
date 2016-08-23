package selling.sunshine.service;

import common.sunshine.model.selling.express.Express4Agent;
import common.sunshine.model.selling.express.Express4Customer;
import common.sunshine.utils.ResultData;

import java.util.Map;

public interface ExpressService {

    ResultData createExpress(Express4Agent express);

    ResultData createExpress(Express4Customer express);

    ResultData fetchExpress(Map<String, Object> condition);

    ResultData fetchExpress4Agent(Map<String, Object> condition);

    ResultData fetchExpress4Customer(Map<String, Object> condition);

    ResultData traceExpress(String expressNo, String type);
    
    ResultData receiveCheck();
}
