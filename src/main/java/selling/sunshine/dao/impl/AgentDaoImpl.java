package selling.sunshine.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import selling.sunshine.dao.AgentDao;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.model.Agent;
import selling.sunshine.model.Credit;
import selling.sunshine.model.Role;
import selling.sunshine.model.User;
import selling.sunshine.model.gift.GiftConfig;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代理商与持久层交互实现类
 * Created by sunshine on 4/8/16.
 */
public class AgentDaoImpl extends BaseDao implements AgentDao {
    private Logger logger = LoggerFactory.getLogger(AgentDaoImpl.class);

    private Object lock = new Object();

    /**
     * 插入代理商记录,同时在user表中也需要添加相应记录
     *
     * @param agent
     * @return
     */
    @Transactional
    @Override
    public ResultData insertAgent(Agent agent) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                agent.setAgentId(IDGenerator.generate("AGT"));
                sqlSession.insert("selling.agent.insert", agent);
                User user = new User(agent.getPhone(), agent.getPassword());
                user.setUserId(IDGenerator.generate("USR"));
                Role role = new Role();
                role.setRoleId("ROL00000002");
                user.setRole(role);
                user.setAgent(new selling.sunshine.model.lite.Agent(agent));
                sqlSession.insert("selling.user.insert", user);
                result.setData(agent);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    /**
     * 根据条件查询符合条件的代理商列表
     *
     * @param condition
     * @return
     */
    @Override
    public ResultData queryAgent(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            condition = handle(condition);
            List<Agent> list = sqlSession.selectList("selling.agent.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    /**
     * 后台分页查询代理商列表
     *
     * @param condition
     * @param param
     * @return
     */
    @Override
    public ResultData queryAgentByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<Agent> page = new DataTablePage<>(param);
        condition = handle(condition);
        ResultData total = queryAgent(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List<Agent>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<Agent>) total.getData()).size());
        List<Agent> current = queryAgentByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    /**
     * 更新代理商信息,同时更新相应user表中的必要信息
     *
     * @param agent
     * @return
     */
    @Transactional
    @Override
    public ResultData updateAgent(Agent agent) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.agent.update", agent);
                if (!StringUtils.isEmpty(agent.getPassword())) {
                    User user = new User(agent.getPhone(), agent.getPassword());
                    user.setAgent(new selling.sunshine.model.lite.Agent(agent));
                    sqlSession.update("selling.user.update", user);
                }
                result.setData(agent);
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
    public ResultData updateAgentCoffer(Agent agent) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.agent.updateCoffer", agent);
                if (!StringUtils.isEmpty(agent.getPassword())) {
                    User user = new User(agent.getPhone(), agent.getPassword());
                    user.setAgent(new selling.sunshine.model.lite.Agent(agent));
                    sqlSession.update("selling.user.update", user);
                }
                result.setData(agent);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    /**
     * 根据微信的openId解绑代理商账号
     *
     * @param openId
     * @return
     */
    @Transactional
    @Override
    public ResultData unbindAgent(String openId) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                Map<String, Object> condition = new HashMap<>();
                condition.put("wechat", openId);
                Agent agent = sqlSession.selectOne("selling.agent.query", condition);
                if (agent == null) {
                    result.setResponseCode(ResponseCode.RESPONSE_NULL);
                }
                sqlSession.update("selling.agent.unbind", openId);
                condition.clear();
                condition.put("agentId", agent.getAgentId());
                agent = sqlSession.selectOne("selling.agent.query", condition);
                result.setData(agent);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }

    }

    /**
     * 查询某一页的代理商的列表信息
     *
     * @param condition
     * @param start
     * @param length
     * @return
     */
    private List<Agent> queryAgentByPage(Map<String, Object> condition, int start, int length) {
        List<Agent> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.agent.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData queryCredit(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Credit> list = sqlSession.selectList("selling.agent.credit.query", condition);
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
    public ResultData insertCredit(Credit credit) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                credit.setCreditId(IDGenerator.generate("CRE"));
                sqlSession.insert("selling.agent.credit.insert", credit);
                result.setData(credit);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    @Transactional
    @Override
    public ResultData insertAgentGift(GiftConfig gift) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                gift.setGiftId(IDGenerator.generate("AGG"));
                sqlSession.insert("selling.agent.gift.insert", gift);
                result.setData(gift);
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
    public ResultData queryAgentGift(Map<String, Object> condition) {
        ResultData result = new ResultData();
        condition = handle(condition);
        try {
            List<GiftConfig> list = sqlSession.selectList("selling.agent.gift.query", condition);
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
