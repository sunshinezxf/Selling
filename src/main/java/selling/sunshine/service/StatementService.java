package selling.sunshine.service;

import java.util.Map;

import common.sunshine.utils.ResultData;

public interface StatementService {
	
	ResultData payedBillStatement(Map<String, Object> condition);
	
	ResultData refundBillStatement(Map<String, Object> condition);
}
