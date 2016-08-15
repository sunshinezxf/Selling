package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.CashBackService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import selling.sunshine.vo.cashback.CashBack4Agent;
import selling.sunshine.vo.cashback.CashBack4AgentPerMonth;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunshine on 8/12/16.
 */
@RestController
@RequestMapping("/cashback")
public class CashBackController {
    private Logger logger = LoggerFactory.getLogger(CashBackController.class);

    @Autowired
    private CashBackService cashBackService;

    @RequestMapping(method = RequestMethod.GET, value = "/month")
    public ModelAndView monthly() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/refund/cashback_month_detail");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/month")
    public DataTablePage<CashBack4AgentPerMonth> monthly(DataTableParam param) {
        DataTablePage<CashBack4AgentPerMonth> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        ResultData response = cashBackService.fetchCashBackPerMonth(condition, param);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<CashBack4AgentPerMonth>) response.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/refund/refund_record");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public DataTablePage<CashBack4Agent> overview(DataTableParam param) {
        DataTablePage<CashBack4Agent> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        ResultData response = cashBackService.fetchCashBack(condition, param);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<CashBack4Agent>) response.getData();
        }
        return result;
    }
}
