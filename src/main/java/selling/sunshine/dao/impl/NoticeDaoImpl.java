package selling.sunshine.dao.impl;

import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.notice.Notice;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import selling.sunshine.dao.NoticeDao;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 2016/12/17.
 */
@Repository
public class NoticeDaoImpl extends BaseDao implements NoticeDao {
    private Logger logger = LoggerFactory.getLogger(NoticeDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insert(Notice notice) {
        ResultData result = new ResultData();
        notice.setNoticeId(IDGenerator.generate("NTE"));
        synchronized (lock) {
            try {
                sqlSession.insert("selling.notice.insert", notice);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Notice> list = sqlSession.selectList("selling.notice.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("未查询到公告信息");
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
