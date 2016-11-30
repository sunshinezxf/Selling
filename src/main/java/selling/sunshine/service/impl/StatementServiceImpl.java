package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import common.sunshine.utils.ResultData;
import selling.sunshine.dao.StatementDao;
import selling.sunshine.service.StatementService;

public class StatementServiceImpl implements StatementService {
	
    private Logger logger = LoggerFactory.getLogger(StatementServiceImpl.class);
    
    @Autowired
    private StatementDao statementDao;

	@Override
	public ResultData eventStatistic() {
		// TODO Auto-generated method stub
		return null;
	}


}
