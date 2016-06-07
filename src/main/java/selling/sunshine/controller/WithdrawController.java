package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by sunshine on 6/7/16.
 */
@RestController
@RequestMapping("/withdraw")
public class WithdrawController {
    private Logger logger = LoggerFactory.getLogger(WithdrawController.class);

    public ModelAndView check() {
        ModelAndView view = new ModelAndView();
        
        return view;
    }
}
