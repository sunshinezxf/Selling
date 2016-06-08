package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.RoleDao;
import selling.sunshine.model.Role;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/3/16.
 */
@Repository
public class RoleDaoImpl extends BaseDao implements RoleDao {
    private Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

    private Object lock = new Object();

    @Transactional
    @Override
    public ResultData insertRole(Role role) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                role.setRoleId(IDGenerator.generate("ROL"));
                sqlSession.insert("selling.role.insert", role);
                result.setData(role);
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
    public ResultData queryRole(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Role> list = sqlSession.selectList("selling.role.query", condition);
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
