package selling.sunshine.service;

import java.util.Map;

import selling.sunshine.model.Express;
import selling.sunshine.utils.ResultData;

public interface ExpressService {
	
	ResultData createExpress(Express express);
	
	ResultData fetchExpress(Map<String, Object> condition);

}
