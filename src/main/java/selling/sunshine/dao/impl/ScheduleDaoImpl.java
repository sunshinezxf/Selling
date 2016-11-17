package selling.sunshine.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.agent.AgentKPI;
import common.sunshine.model.selling.customer.CustomerAddress;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.ScheduleDao;

public class ScheduleDaoImpl extends BaseDao implements ScheduleDao{
	

	private Logger logger = LoggerFactory.getLogger(ScheduleDaoImpl.class);

	private Object lock = new Object();

	@Override
	public ResultData updateCustomerAddress(CustomerAddress customerAddress) {
		ResultData result = new ResultData();
		synchronized (lock) {
			try {
				sqlSession.update("selling.customer.address.update", customerAddress);
				result.setData(customerAddress);
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
	public ResultData insertAgentKPI(AgentKPI agentKPI) {
		ResultData result = new ResultData();
		synchronized (lock) {
			try {
				agentKPI.setKpiId(IDGenerator.generate("KPI"));
				sqlSession.insert("selling.agent.kpi.insert", agentKPI);
				result.setData(agentKPI);
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
