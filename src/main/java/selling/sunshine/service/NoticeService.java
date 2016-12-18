package selling.sunshine.service;

import common.sunshine.model.selling.notice.Notice;
import common.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 2016/12/17.
 */
public interface NoticeService {
    ResultData fetchNotice(Map<String, Object> condition);

    ResultData createNotice(Notice notice);
}
