package selling.sunshine.service;

import java.util.List;

import selling.sunshine.utils.ResultData;

public interface DeliverService {
	ResultData generateDeliver();

	<T> ResultData produce(List<T> list);
}
