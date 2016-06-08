package selling.sunshine.dao;

import java.util.Map;

import selling.sunshine.model.Express;
import selling.sunshine.utils.ResultData;

public interface ExpressDao {
	
	ResultData insertExpress(Express express );
	
	ResultData queryExpress(Map<String, Object> condition);

}
