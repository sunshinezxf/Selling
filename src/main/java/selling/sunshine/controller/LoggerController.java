package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sunshine on 7/15/16.
 */
@RequestMapping("/log")
@RestController
public class LoggerController {
    private Logger logger = LoggerFactory.getLogger(LoggerController.class);


}
