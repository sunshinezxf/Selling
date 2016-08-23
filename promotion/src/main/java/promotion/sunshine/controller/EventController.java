package promotion.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sunshine on 8/22/16.
 */
@RestController
@RequestMapping("/event")
public class EventController {
    private Logger logger = LoggerFactory.getLogger(EventController.class);
}
