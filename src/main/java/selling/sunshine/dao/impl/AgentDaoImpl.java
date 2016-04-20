package selling.sunshine.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import selling.sunshine.dao.AgentDao;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.model.Agent;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
public class AgentDaoImpl extends BaseDao implements AgentDao {
    private Logger logger = LoggerFactory.getLogger(AgentDaoImpl.class);

    @Transactional
    @Override
    public ResultData insertAgent(Agent agent) {
        ResultData result = new ResultData();
        Object lock = new Object();
        synchronized (lock) {
            try {
                agent.setAgentId(IDGenerator.generate("AGT"));
                sqlSession.insert("selling.agent.insert", agent);
                result.setData(agent);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result = insertAgent(agent);
            } finally {
                return result;
            }
        }
    }

    @Override
    public ResultData queryAgent(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
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


    @Override
    public ResultData queryAgentByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<Agent> page = new DataTablePage<Agent>();
        page.setsEcho(param.getsEcho());
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

    private List<Agent> queryAgentByPage(Map<String, Object> condition, int start, int length) {
        List<Agent> result = new ArrayList<Agent>();
        try {
            result = sqlSession.selectList("selling.agent.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }
}
