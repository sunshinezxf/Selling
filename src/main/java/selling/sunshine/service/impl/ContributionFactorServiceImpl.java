package selling.sunshine.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.ContributionFactorDao;
import selling.sunshine.model.ContributionFactor;
import selling.sunshine.service.ContributionFactorService;

public class ContributionFactorServiceImpl implements ContributionFactorService{
	
	private Logger logger = LoggerFactory.getLogger(ContributionFactorServiceImpl.class);
	
	@Autowired
	private ContributionFactorDao contributionFactorDao;

	@Override
	public ResultData createContributionFactor(ContributionFactor contributionFactor) {
		ResultData result = new ResultData();
		ResultData insertResponse = contributionFactorDao.insertContributionFactor(contributionFactor);
		result.setResponseCode(insertResponse.getResponseCode());
		if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(insertResponse.getData());
		} else {
			result.setDescription(insertResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData updateContributionFactor(ContributionFactor contributionFactor) {
		ResultData result = new ResultData();
		ResultData updateResponse = contributionFactorDao.updateContributionFactor(contributionFactor);
		result.setResponseCode(updateResponse.getResponseCode());
		if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(updateResponse.getData());
		} else {
			result.setDescription(updateResponse.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchContributionFactor(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData queryResponse = contributionFactorDao.queryContributionFactor(condition);
		result.setResponseCode(queryResponse.getResponseCode());
		if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
			if (((List<ContributionFactor>) queryResponse.getData()).size() == 0) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
			} else {
				result.setData(queryResponse.getData());
			}
		} else {
			result.setDescription(queryResponse.getDescription());
		}
		return result;
	}

}
