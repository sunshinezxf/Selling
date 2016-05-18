package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import selling.sunshine.utils.ResultData;

/**
 * Created by sunshine on 5/10/16.
 */
@RequestMapping("/bill")
@RestController
public class BillController {

    private Logger logger = LoggerFactory.getLogger(BillController.class);

    @ResponseBody
    @RequestMapping("/{billId}/inform")
    public ResultData inform() {
        ResultData result = new ResultData();

        return result;
    }
}
