package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 7/14/16.
 */
@RequestMapping("/message")
@RestController
public class MessageController {
    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    public ResultData message() {
        ResultData result = new ResultData();

        return result;
    }
}
