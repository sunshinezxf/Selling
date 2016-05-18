package selling.sunshine.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;

import selling.sunshine.model.Agent;
import selling.sunshine.model.BillStatus;
import selling.sunshine.model.DepositBill;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.BillService;
import selling.sunshine.service.ToolService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/10/16.
 */
@RequestMapping("/bill")
@RestController
public class BillController {

    private Logger logger = LoggerFactory.getLogger(BillController.class);
    
    @Autowired
    private ToolService toolService;
    
    @Autowired
    private BillService billService;
    
    @Autowired
    private AgentService agentService;


    @ResponseBody
    @RequestMapping("/inform")
    public ResultData inform(HttpServletRequest request, HttpServletResponse response) {
        ResultData resultData = new ResultData();
        
        JSONObject webhooks = toolService.getParams(request);
        logger.debug("webhooks info == " + webhooks);
        JSONObject charge = webhooks.getJSONObject("data").getJSONObject("object");
        logger.debug("charge info == " + charge);
        String dealId = charge.getString("order_no");
        logger.debug("deal id: " + dealId);
        
        if (StringUtils.isEmpty(dealId)) {
			resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
			return resultData;
		}
        
        if (dealId.contains("ODB")) {
        	Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("billId", dealId);
            resultData = billService.fetchDepositBill(condition);
            DepositBill depositBill = ((List<DepositBill>) resultData.getData()).get(0);
            
            Agent agent = depositBill.getAgent();
            agent.setCoffer(agent.getCoffer()+depositBill.getBillAmount());
            resultData = agentService.updateAgent(agent);
            depositBill.setStatus(BillStatus.PAYED);
            resultData = billService.updateDepositBill(depositBill);

            Event event = Webhooks.eventParse(webhooks.toString());
            if ("charge.succeeded".equals(event.getType())) {
                response.setStatus(200);
            } else if ("refund.succeeded".equals(event.getType())) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }			
		}
        
        return resultData;
    }
}
