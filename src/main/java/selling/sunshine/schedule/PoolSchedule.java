package selling.sunshine.schedule;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import selling.sunshine.service.OrderService;
import selling.sunshine.service.RefundService;

/**
 * Created by sunshine on 5/23/16.
 */
public class PoolSchedule {
    private Logger logger = LoggerFactory.getLogger(PoolSchedule.class);
    
    @Autowired
    private OrderService orderService; 
    
    @Autowired
    private RefundService refundService;

    public void schedule() {
    	orderService.poolOrder();
    	
    }
    
    public void refundSchedule() {
    	refundService.refundRecord();
	}
    
    public void checkOrderPool(){
    	Map<String, Object> condition=new HashMap<>();
    	condition.put("blockFlag", false);
    	condition.put("monthConfig", 4);
    	orderService.checkOrderPool(condition);
    }
}
