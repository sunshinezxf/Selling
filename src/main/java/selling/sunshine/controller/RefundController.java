package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.model.RefundConfig;
import selling.sunshine.service.RefundService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 5/17/16.
 */
@RequestMapping("/refund")
@RestController
public class RefundController {
    private Logger logger = LoggerFactory.getLogger(RefundController.class);

    @Autowired
    private RefundService refundService;

    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public ModelAndView config() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/refund/config");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/config/goods/{goodsId}")
    public ModelAndView config(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();

        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/config/goods/{goodsId}")
    public ResultData goodsConfig(@PathVariable("goodsId") String goodsId) {
        ResultData result = new ResultData();
        Map<String, Object> condtion = new HashMap<>();
        condtion.put("goodsId", goodsId);
        ResultData fetchResponse = refundService.fetchRefundConfig(condtion);
        result.setResponseCode(fetchResponse.getResponseCode());
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(((List<RefundConfig>) fetchResponse.getData()).get(0));
        }
        return result;
    }
}
