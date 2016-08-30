package selling.sunshine.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.service.RefundService;

/**
 * Created by sunshine on 6/24/16.
 */
public class RefundSchedule {
    private Logger logger = LoggerFactory.getLogger(PoolSchedule.class);

    @Autowired
    private RefundService refundService;

    public void schedule() {
    	
        refundService.refund();
    }
}
