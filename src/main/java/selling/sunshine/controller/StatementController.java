package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import selling.sunshine.service.StatementService;

public class StatementController {
	
    private Logger logger = LoggerFactory.getLogger(StatementController.class);
    
    @Autowired
    private StatementService statementService;


}
