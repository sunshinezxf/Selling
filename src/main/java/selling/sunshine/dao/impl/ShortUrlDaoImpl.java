package selling.sunshine.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.event.GiftEvent;
import common.sunshine.model.selling.util.ShortUrl;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.ShortUrlDao;

@Repository
public class ShortUrlDaoImpl extends BaseDao implements ShortUrlDao {
	private Logger logger = LoggerFactory.getLogger(EventDaoImpl.class);

    private Object lock = new Object();
    
	@Override
	public ResultData insertShortUrl(ShortUrl shortUrl) {
		ResultData result = new ResultData();
        synchronized (lock) {
            try {
            	shortUrl.setUrlId(IDGenerator.generate("SHU"));
                sqlSession.insert("selling.shorturl.insert", shortUrl);    
                result.setData(shortUrl);
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
	public ResultData queryShortUrl(Map<String, Object> condition) {
		ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<ShortUrl> list = sqlSession.selectList("selling.shorturl.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
	}

}
