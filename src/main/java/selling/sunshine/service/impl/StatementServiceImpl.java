package selling.sunshine.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.dao.StatementDao;
import selling.sunshine.service.EventService;
import selling.sunshine.service.StatementService;

public class StatementServiceImpl implements StatementService {
	
    private Logger logger = LoggerFactory.getLogger(StatementServiceImpl.class);
    
    @Autowired
    private StatementDao statementDao;
    
    @Autowired
    private EventService eventService;

	@Override
	public ResultData eventStatistic() {
		//1.查询所有的event
		Map<String, Object> condition=new HashMap<>();
		ResultData queryData=new ResultData();
		queryData=eventService.fetchEventApplication(condition);
		if (queryData.getResponseCode()==ResponseCode.RESPONSE_OK) {
			
		}
		return null;
	}


}
