package selling.sunshine.service;

import java.util.List;

import selling.sunshine.utils.ResultData;

public interface GatherService {
	ResultData generateGather();

	<T> ResultData produce(List<T> list);
}
