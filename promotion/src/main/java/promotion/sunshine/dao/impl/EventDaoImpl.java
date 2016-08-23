package promotion.sunshine.dao.impl;

import common.sunshine.dao.BaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import promotion.sunshine.dao.EventDao;

/**
 * Created by sunshine on 8/23/16.
 */
@Repository
public class EventDaoImpl extends BaseDao implements EventDao {
    private Logger logger = LoggerFactory.getLogger(EventDaoImpl.class);
}
