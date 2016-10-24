package selling.sunshine.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.AgentVitalityDao;
import selling.sunshine.model.AgentVitality;
import selling.sunshine.service.AgentVitalityService;

public class AgentVitalityServiceImpl implements AgentVitalityService {

	private Logger logger = LoggerFactory.getLogger(AgentVitalityServiceImpl.class);

	@Autowired
	private AgentVitalityDao agentVitalityDao;

	@Override
	public ResultData createAgentVitality(AgentVitality agentVitality) {
		ResultData result = new ResultData();
		ResultData insertResponse = agentVitalityDao.insertAgentVitality(agentVitality);
		result.setResponseCode(insertResponse.getResponseCode());
		if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		} else {
			result.setDescription(insertResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchAgentVitality(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = agentVitalityDao.queryAgentVitality(condition);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List<AgentVitality>) queryResponse.getData()).size() == 0) {
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
	public ResultData fetchAgentVitalityByPage(Map<String, Object> condition, DataTableParam param) {
		ResultData resultData = new ResultData();
		ResultData queryResponse = agentVitalityDao.queryAgentVitalityByPage(condition, param);
		resultData.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			resultData.setData(queryResponse.getData());
		} else {
			resultData.setDescription(queryResponse.getDescription());
		}
		return resultData;
	}

	@Override
	public ResultData updateAgentVitality(AgentVitality agentVitality) {
        ResultData result = new ResultData();
        ResultData updateResponse = agentVitalityDao.updateAgentVitality(agentVitality);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
	}

}
