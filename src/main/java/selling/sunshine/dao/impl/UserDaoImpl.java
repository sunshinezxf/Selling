package selling.sunshine.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import common.sunshine.dao.BaseDao;
import selling.sunshine.dao.UserDao;
import common.sunshine.model.selling.user.User;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/3/16.
 */
@Repository
public class UserDaoImpl extends BaseDao implements UserDao {
    private Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private Object lock = new Object();

    @Transactional
    @Override
    public ResultData insertUser(User user) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.insert("selling.user.insert", user);
                if (user.getAdmin() != null) {
                    sqlSession.insert("selling.admin.insert", user.getAdmin());
                }
                if (user.getAgent() != null) {
                    sqlSession.insert("selling.agent.insert", user.getAgent());
                }
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
    public ResultData queryUser(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<User> list = sqlSession.selectList("selling.user.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData queryUser(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<User> page = new DataTablePage<>(param);
        condition = handle(condition);
        ResultData total = queryUser(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List<User>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<User>) total.getData()).size());
        List<User> current = queryUserByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    private List<User> queryUserByPage(Map<String, Object> condition, int start, int length) {
        List<User> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.user.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }
}
