package selling.sunshine.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.service.ExpressService;

/**
 * Created by sunshine on 7/13/16.
 */
public class ExpressSchedule {
    private Logger logger = LoggerFactory.getLogger(ExpressSchedule.class);

    @Autowired
    private ExpressService expressService;

    public void schedule() {
        expressService.receiveCheck();
    }
}
