package selling.sunshine.service;

import selling.sunshine.model.express.Express;
import common.sunshine.utils.ResultData;

import java.util.List;

public interface DeliverService {
    ResultData generateDeliver();

    <T> ResultData produce(List<T> list);

    ResultData produceSummary(List<Express> list);
}
