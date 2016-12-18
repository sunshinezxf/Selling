package selling.sunshine.dao;

import common.sunshine.model.selling.notice.Notice;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 2016/12/17.
 */
public interface NoticeDao {
    ResultData insert(Notice notice);

    ResultData query(Map<String, Object> condition);
}
