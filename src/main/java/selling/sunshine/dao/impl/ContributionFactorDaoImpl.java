package selling.sunshine.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import common.sunshine.dao.BaseDao;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.ContributionFactorDao;
import selling.sunshine.model.ContributionFactor;

public class ContributionFactorDaoImpl extends BaseDao implements ContributionFactorDao {

	private Logger logger = LoggerFactory.getLogger(ContributionFactorDaoImpl.class);

	private Object lock = new Object();

	@Override
	public ResultData insertContributionFactor(ContributionFactor contributionFactor) {
		ResultData result = new ResultData();
		contributionFactor.setFactorId(IDGenerator.generate("FAC"));
		synchronized (lock) {
			try {
				sqlSession.insert("selling.contributionFactor.insert", contributionFactor);
				result.setData(contributionFactor);
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
	public ResultData queryContributionFactor(Map<String, Object> condition) {
		ResultData result = new ResultData();
		condition = handle(condition);
		try {
			List<ContributionFactor> list = sqlSession.selectList("selling.contributionFactor.query", condition);
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
	public ResultData updateContributionFactor(ContributionFactor contributionFactor) {
		ResultData result = new ResultData();
		synchronized (lock) {
			try {
				sqlSession.update("selling.contributionFactor.update", contributionFactor);
				result.setData(contributionFactor);
			} catch (Exception e) {
				logger.error(e.getMessage());
				result.setResponseCode(ResponseCode.RESPONSE_ERROR);
				result.setDescription(e.getMessage());
			} finally {
				return result;
			}
		}
	}

}
