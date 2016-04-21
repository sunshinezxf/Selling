package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import selling.sunshine.dao.AdminDao;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.model.Admin;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/21/16.
 */
@Repository
public class AdminDaoImpl extends BaseDao implements AdminDao {
    private Logger logger = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Override
    public ResultData queryAdmin(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Admin> list = sqlSession.selectList("selling.admin.query", condition);
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
