package selling.sunshine.dao;

import java.util.Map;

import common.sunshine.utils.ResultData;

public interface StatementDao {
	
	ResultData payedBillStatement(Map<String, Object> condition);
	
	ResultData refundBillStatement(Map<String, Object> condition);

}
