package selling.sunshine.service;

import java.util.Map;

import common.sunshine.utils.ResultData;
import selling.sunshine.model.ContributionFactor;

public interface ContributionFactorService {
	ResultData createContributionFactor(ContributionFactor contributionFactor);
	
	ResultData updateContributionFactor(ContributionFactor contributionFactor);
	
	ResultData fetchContributionFactor(Map<String, Object> condition);
}
