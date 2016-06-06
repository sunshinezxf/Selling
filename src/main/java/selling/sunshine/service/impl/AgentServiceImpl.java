package selling.sunshine.service.impl;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import selling.sunshine.dao.AgentDao;
import selling.sunshine.model.Agent;
import selling.sunshine.model.Credit;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.MessageService;
import selling.sunshine.utils.Encryption;
import selling.sunshine.utils.PasswordGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 4/8/16.
 */
@Service
public class AgentServiceImpl implements AgentService {
    private Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

    @Autowired
    private AgentDao agentDao;

    @Autowired
    private MessageService messageService;

    @Override
    public ResultData login(Agent agent) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        agent.setPassword(Encryption.md5(agent.getPassword()));
        condition.put("phone", agent.getPhone());
        ResultData queryResponse = agentDao.queryAgent(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Agent>) queryResponse.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                return result;
            }
        }
        Agent target = ((List<Agent>) queryResponse.getData()).get(0);
        if (agent.getPhone().equals(target.getPhone()) && agent.getPassword().equals(target.getPassword())) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(target);
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        return result;
    }

    @Override
    public ResultData fetchAgent(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = agentDao.queryAgent(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Agent>) queryResponse.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData createAgent(Agent agent) {
        ResultData result = new ResultData();
        agent.setPassword(Encryption.md5(agent.getPassword()));
        ResultData insertResponse = agentDao.insertAgent(agent);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchAgent(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = agentDao.queryAgentByPage(condition, param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData placeOrder(Map<String, Object> conditon) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultData updateAgent(Agent agent) {
        ResultData result = new ResultData();
        ResultData updateResponse = agentDao.updateAgent(agent);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData consume(Agent agent, double money) {
        ResultData result = new ResultData();
        if (agent.getCoffer() < money) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("账户余额不足");
            return result;
        }
        agent.setCoffer(agent.getCoffer() - money);
        ResultData updateResponse = updateAgent(agent);
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData unbindAgent(String openId) {
        ResultData result = new ResultData();
        ResultData unbindResponse = agentDao.unbindAgent(openId);
        result.setResponseCode(unbindResponse.getResponseCode());
        if (unbindResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setDescription(unbindResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData resetPassword(Agent agent) {
        ResultData result = new ResultData();
        String password = PasswordGenerator.generate();
        messageService.send(agent.getPhone(), "尊敬的代理商您好,您的账户密码已经重置为:" + password + ",请尽快登录并及时修改您的密码.【云草纲目】");
        agent.setPassword(Encryption.md5(password));
        ResultData updateResponse = agentDao.updateAgent(agent);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData modifyPassword(Agent agent, String password) {
        ResultData result = new ResultData();
        agent.setPassword(Encryption.md5(password));
        ResultData updateResponse = agentDao.updateAgent(agent);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

	@Override
	public ResultData fetchCredit(Map<String, Object> condition) {
		ResultData result = new ResultData();
        ResultData queryResponse = agentDao.queryCredit(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Agent>) queryResponse.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
	}

	@Override
	public ResultData createCredit(Credit credit) {
		ResultData result = new ResultData();
        ResultData insertResponse =agentDao.insertCredit(credit);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
	}
}
