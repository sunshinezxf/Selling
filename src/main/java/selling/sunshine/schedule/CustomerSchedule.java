package selling.sunshine.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.service.CustomerService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wxd on 2016/12/21.
 */
public class CustomerSchedule {

    private Logger logger = LoggerFactory.getLogger(CustomerSchedule.class);

    @Autowired
    private CustomerService customerService;

    public void schedule() {
        Map<String, Object> condition = new HashMap<>();
        customerService.customerTransform(condition);
    }
}
