package selling.sunshine.service;

import common.sunshine.utils.ResultData;

import java.util.List;

/**
 * Created by sunshine on 7/7/16.
 */
public interface IndentService {
    ResultData generateIndent();

    <T> ResultData produce(List<T> list);

    <T> ResultData produceAll(List<T> list);

    ResultData produceSummary(List list);
}
