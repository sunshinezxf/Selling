package selling.sunshine.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import selling.sunshine.service.StatementService;

public class StatementController {
	
    private Logger logger = LoggerFactory.getLogger(StatementController.class);
    
    @Autowired
    private StatementService statementService;

    @RequestMapping(method = RequestMethod.GET, value = "/downloadGiftExcel")
    public String downloadGiftExcel(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException, WriteException {
    	Map<String, Object> condition = new HashMap<String, Object>();
    	
    	return null;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/downloadEventExcel")
    public String downloadEventExcel(HttpServletRequest request, HttpServletResponse response) throws IOException, RowsExceededException, WriteException {
    	return null;
    }
}
