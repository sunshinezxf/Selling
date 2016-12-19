package selling.sunshine.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.service.OrderService;

/**
 * Created by sunshine on 2016/12/19.
 */
public class PromotionSchedule {
    private Logger logger = LoggerFactory.getLogger(PoolSchedule.class);

    @Autowired
    private OrderService orderService;

    public void schedule() {
        orderService.n4mScanner();
    }
}
