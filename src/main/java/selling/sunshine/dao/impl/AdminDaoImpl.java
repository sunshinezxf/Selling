package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import selling.sunshine.dao.AdminDao;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.model.Admin;
import selling.sunshine.model.Role;
import selling.sunshine.model.User;
import selling.sunshine.utils.IDGenerator;
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
	private Object lock = new Object();

	@Override
	public ResultData queryAdmin(Map<String, Object> condition) {
		ResultData result = new ResultData();
		try {
			List<Admin> list = sqlSession.selectList("selling.admin.query",
					condition);
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		} finally {
			return result;
		}
	}

	@Transactional
	@Override
	public ResultData insertAdmin(Admin admin) {
		ResultData result = new ResultData();
		synchronized (lock) {
			try {
				admin.setAdminId(IDGenerator.generate("MNG"));
				sqlSession.insert("selling.admin.insert", admin);
				User user = new User(admin);
				user.setUserId(IDGenerator.generate("USR"));
				Role role = new Role();
				role.setRoleId("ROL00000001");
				user.setRole(role);
				user.setAdmin(admin);
				;
				sqlSession.insert("selling.user.insert", user);
				result.setData(admin);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result = insertAdmin(admin);
			} finally {
				return result;
			}
		}
	}

	@Transactional
	@Override
	public ResultData updateAdmin(Admin admin) {
		ResultData result = new ResultData();
		synchronized (lock) {
			try {
				sqlSession.update("selling.admin.update", admin);
				result.setData(admin);
			} catch (Exception e) {
				logger.debug(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result.setDescription(e.getMessage());
			} finally {
				return result;
			}
		}
	}

	@Transactional
	@Override
	public ResultData deleteAdmin(Admin admin) {
		ResultData result = new ResultData();
		synchronized (lock) {
			try {
				sqlSession.delete("selling.admin.delete", admin);
				result.setData(admin);
			} catch (Exception e) {
				logger.debug(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result.setDescription(e.getMessage());
			} finally {
				return result;
			}
		}
	}
}
