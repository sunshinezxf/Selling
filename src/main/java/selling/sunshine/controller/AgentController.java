package selling.sunshine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import selling.sunshine.service.AgentService;

/**
 * Created by sunshine on 4/8/16.
 */
@RequestMapping("/agent")
@RestController
public class AgentController {
    @Autowired
    private AgentService agentService;
}
