package selling.sunshine.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import common.sunshine.model.selling.admin.Admin;
import common.sunshine.model.selling.user.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.RefundConfigForm;
import selling.sunshine.model.*;
import selling.sunshine.model.cashback.CashBackRecord;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import selling.sunshine.service.CommodityService;
import selling.sunshine.service.LogService;
import selling.sunshine.service.RefundService;
import selling.sunshine.service.ToolService;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @Autowired
    private ToolService toolService;

    @Autowired
    private LogService logService;

    @Autowired
    private CommodityService commodityService;

    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public ModelAndView config() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/refund/config");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/config/goods/{goodsId}")
    public ModelAndView config(@PathVariable("goodsId") String goodsId, @Valid RefundConfigForm form, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/refund/config");
            return view;
        }
        Goods4Customer goods = new Goods4Customer();
        Map<String, Object> condition = new HashMap<>();
        condition.put("goodsId", goodsId);
        goods = ((List<Goods4Customer>) commodityService.fetchGoods4Customer(condition).getData()).get(0);
        RefundConfig config = new RefundConfig(goods, Integer.parseInt(form.getAmountTrigger()), Double.parseDouble(form.getLevel1Percent()), Double.parseDouble(form.getLevel2Percent()), Double.parseDouble(form.getLevel3Percent()), Integer.parseInt(form.getMonthConfig()));
        ResultData createResponse = refundService.createRefundConfig(config);
        if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            if (user == null) {
                view.setViewName("redirect:/refund/config");
                return view;
            }
            Admin admin = user.getAdmin();
            BackOperationLog backOperationLog = new BackOperationLog(
                    admin.getUsername(), toolService.getIP(request), "管理员" + admin.getUsername() + "修改了商品名称为" + goods.getName() + "的返现配置");
            logService.createbackOperationLog(backOperationLog);

            view.setViewName("redirect:/refund/config");
            return view;
        }
        view.setViewName("redirect:/refund/config");
        return view;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/config/goods/{goodsId}")
    public ResultData goodsConfig(@PathVariable("goodsId") String goodsId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("goodsId", goodsId);
        condition.put("blockFlag", false);
        ResultData fetchResponse = refundService.fetchRefundConfig(condition);
        result.setResponseCode(fetchResponse.getResponseCode());
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(((List<RefundConfig>) fetchResponse.getData()).get(0));
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/record/overview")
    public ModelAndView refundRecordOverview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/refund/refund_record");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/record/overview")
    public DataTablePage<CashBackRecord> refundRecordOverview(DataTableParam param) {
        DataTablePage<CashBackRecord> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", true);
        ResultData fetchResponse = refundService.fetchRefundRecordByPage(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<CashBackRecord>) fetchResponse.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/record/month")
    public ModelAndView refundRecordMonth() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/refund/refund_record_month");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/record/month")
    public DataTablePage<CashBackRecord> refundRecordMonth(DataTableParam param) {
        DataTablePage<CashBackRecord> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String date = dateFormat.format(c.getTime());
        condition.put("createAt", date + "%");
        condition.put("blockFlag", false);

        ResultData fetchResponse = refundService.fetchRefundRecordByPage(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<CashBackRecord>) fetchResponse.getData();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/statistic/{agentId}")
    public JSONObject statistic(@PathVariable("agentId") String agentId) {
        JSONObject result = new JSONObject();
        Map<String, Object> condition = new HashMap<>();
        condition.put("agentId", agentId);
        if (refundService.statistic(condition).getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) refundService.statistic(condition).getData();
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

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/calculateRefund")
    public ResultData calculateRefund() {
        ResultData resultData = new ResultData();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null || user.getAgent() == null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            resultData.setDescription("请重新登录");
            return resultData;
        }
        resultData = refundService.calculateRefund(user.getAgent().getAgentId());
        return resultData;
    }


}
