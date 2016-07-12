package selling.sunshine.service;

import selling.sunshine.utils.ResultData;

import java.util.List;

/**
 * Created by sunshine on 7/7/16.
 */
public interface IndentService {
    ResultData generateIndent();

    <T> ResultData produce(List<T> list);
}
