package selling.sunshine.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.service.OrderService;


public class CustomerOrderSchedule {

    private Logger logger = LoggerFactory.getLogger(CustomerOrderSchedule.class);

    @Autowired
    private OrderService orderService;

    public void schedule() {
        orderService.check();
        orderService.fullFillCusOrder();
    }

}
