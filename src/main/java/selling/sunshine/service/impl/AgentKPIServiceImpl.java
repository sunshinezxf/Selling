package selling.sunshine.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import common.sunshine.model.selling.agent.AgentKPI;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.AgentKPIDao;
import selling.sunshine.service.AgentKPIService;

public class AgentKPIServiceImpl implements AgentKPIService {

	private Logger logger = LoggerFactory.getLogger(AgentKPIServiceImpl.class);

	@Autowired
	private AgentKPIDao agentKPIDao;

	@Override
	public ResultData createAgentKPI(AgentKPI agentKPI) {
		ResultData result = new ResultData();
		ResultData insertResponse = agentKPIDao.insertAgentKPI(agentKPI);
		result.setResponseCode(insertResponse.getResponseCode());
		if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		} else {
			result.setDescription(insertResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData updateAgentKPI(AgentKPI agentKPI) {
		ResultData result = new ResultData();
		ResultData updateResponse = agentKPIDao.updateAgentKPI(agentKPI);
		result.setResponseCode(updateResponse.getResponseCode());
		if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(updateResponse.getData());
		} else {
			result.setDescription(updateResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchAgentKPI(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = agentKPIDao.queryAgentKPI(condition);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List<AgentKPI>) queryResponse.getData()).size() == 0) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			} else {
				result.setData(queryResponse.getData());
			}
		} else {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchAgentKPIByPage(Map<String, Object> condition, DataTableParam param) {
		ResultData resultData = new ResultData();
		ResultData queryResponse = agentKPIDao.queryAgentKPIByPage(condition, param);
		resultData.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			resultData.setData(queryResponse.getData());
		} else {
			resultData.setDescription(queryResponse.getDescription());
		}
		return resultData;
	}

}
