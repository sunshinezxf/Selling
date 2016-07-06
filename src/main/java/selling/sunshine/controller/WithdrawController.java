package selling.sunshine.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.model.WithdrawRecord;
import selling.sunshine.model.WithdrawStatus;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.ToolService;
import selling.sunshine.service.WithdrawService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 6/7/16.
 */
@RestController
@RequestMapping("/withdraw")
public class WithdrawController {
    private Logger logger = LoggerFactory.getLogger(WithdrawController.class);

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private ToolService toolService;

    @RequestMapping(method = RequestMethod.GET, value = "/check")
    public ModelAndView check() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/withdraw/check");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/check")
    public DataTablePage<WithdrawRecord> check(DataTableParam param) {
        DataTablePage<WithdrawRecord> result = new DataTablePage<WithdrawRecord>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        List<Integer> status = new ArrayList<Integer>();
        status.add(0);
        condition.put("status", status);
        condition.put("blockFlag", true);
        ResultData fetchResponse = withdrawService.fetchWithdrawRecord(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<WithdrawRecord>) fetchResponse.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/withdraw/overview");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public DataTablePage<WithdrawRecord> overview(DataTableParam param) {
        DataTablePage<WithdrawRecord> result = new DataTablePage<WithdrawRecord>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        ResultData fetchResponse = withdrawService.fetchWithdrawRecord(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<WithdrawRecord>) fetchResponse.getData();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/inform")
    public ResultData inform(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultData result = new ResultData();
        JSONObject webhooks = toolService.getParams(request);
        logger.debug("webhooks info == " + webhooks);
        JSONObject charge = webhooks.getJSONObject("data").getJSONObject("object");
        logger.debug("charge info == " + charge);
        String dealId = charge.getString("order_no");
        logger.debug("deal id: " + dealId);

        //先处理错误状态
        if (StringUtils.isEmpty(dealId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        Event event = Webhooks.eventParse(webhooks.toString());
        if ("transfer.succeeded".equals(event.getType())) {
            response.setStatus(200);
        } else {
            response.setStatus(500);
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        if (dealId.startsWith("WID")) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("withdrawId", dealId);
            ResultData fetchResponse = withdrawService.fetchWithdrawRecord(condition);
            WithdrawRecord record = ((List<WithdrawRecord>) fetchResponse.getData()).get(0);
            record.setStatus(WithdrawStatus.PROCESS);
            result = withdrawService.updateWithdrawRecord(record);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/statistic/{agentId}")
    public JSONObject statistic(@PathVariable("agentId") String agentId) {
        JSONObject result = new JSONObject();
        Map<String, Object> condition = new HashMap<>();
        condition.put("agentId", agentId);
        if (withdrawService.statistic(condition).getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) withdrawService.statistic(condition).getData();
            JSONArray categories = new JSONArray();
            JSONArray data = new JSONArray();
            double totalAmount = 0;
            for (int i = 0; i < list.size(); i++) {
                categories.add(list.get(i).get("date"));
                data.add(list.get(i).get("amount"));
                totalAmount += (double) list.get(i).get("amount");
            }
            result.put("totalAmount", totalAmount);
            result.put("categories", categories);
            result.put("data", data);
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{withdrawId}/send")
    public ModelAndView send(@PathVariable("withdrawId") String withdrawId) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        condition.put("withdrawId", withdrawId);
        ResultData fetchResponse = withdrawService.fetchWithdrawRecord(condition);
        if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/withdraw/check");
            return view;
        }
        WithdrawRecord record = ((List<WithdrawRecord>) fetchResponse.getData()).get(0);
        if (record.isBlockFlag() == true) {
            record.setBlockFlag(false);
            ResultData updateResponse = withdrawService.updateWithdrawRecord(record);
            if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                view.setViewName("redirect:/withdraw/check");
                return view;
            }
        }
        view.setViewName("redirect:/withdraw/check");
        return view;
    }
}
