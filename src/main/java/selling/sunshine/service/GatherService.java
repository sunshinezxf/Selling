package selling.sunshine.service;

import java.util.List;

import common.sunshine.utils.ResultData;

public interface GatherService {
	ResultData generateGather();

	<T> ResultData produce(List<T> list);
	
	ResultData produceSummary(List list);
}
