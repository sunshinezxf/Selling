package selling.sunshine.service.impl;

import common.sunshine.model.selling.notice.Notice;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.NoticeDao;
import selling.sunshine.service.NoticeService;

import java.util.Map;

/**
 * Created by sunshine on 2016/12/17.
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    private Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Autowired
    private NoticeDao noticeDao;

    @Override
    public ResultData fetchNotice(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = noticeDao.query(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData createNotice(Notice notice) {
        ResultData result = new ResultData();
        ResultData response = noticeDao.insert(notice);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }
}
