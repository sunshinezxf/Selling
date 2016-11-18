package selling.sunshine.dao;

import java.util.Map;

import common.sunshine.utils.ResultData;
import selling.sunshine.model.ContributionFactor;

public interface ContributionFactorDao {
	
	ResultData insertContributionFactor(ContributionFactor contributionFactor);
	
	ResultData queryContributionFactor(Map<String, Object> condition);
	
	ResultData updateContributionFactor(ContributionFactor contributionFactor);

}
