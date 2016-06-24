package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import selling.sunshine.form.RefundConfigForm;
import selling.sunshine.model.RefundConfig;
import selling.sunshine.model.RefundRecord;
import selling.sunshine.model.goods.Goods4Customer;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.RefundService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import javax.validation.Valid;

import java.sql.Timestamp;
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

    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public ModelAndView config() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/refund/config");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/config/goods/{goodsId}")
    public ModelAndView config(@PathVariable("goodsId") String goodsId, @Valid RefundConfigForm form, BindingResult result) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/refund/config");
            return view;
        }
        Goods4Customer goods = new Goods4Customer();
        goods.setGoodsId(goodsId);
        RefundConfig config = new RefundConfig(goods, Integer.parseInt(form.getAmountTrigger()), Double.parseDouble(form.getLevel1Percent()),Double.parseDouble(form.getLevel2Percent()),Double.parseDouble(form.getLevel3Percent()));
        ResultData createResponse = refundService.createRefundConfig(config);
        if (createResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
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
    public DataTablePage<RefundRecord> refundRecordOverview(DataTableParam param) {
        DataTablePage<RefundRecord> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", true);
        ResultData fetchResponse = refundService.fetchRefundRecordByPage(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<RefundRecord>) fetchResponse.getData();
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
    public DataTablePage<RefundRecord> refundRecordMonth(DataTableParam param) {
        DataTablePage<RefundRecord> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Timestamp lastMonth = new Timestamp(c.getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String date = dateFormat.format(lastMonth);
        condition.put("createAt", date + "%");
        condition.put("blockFlag", false);
        
        ResultData fetchResponse = refundService.fetchRefundRecordByPage(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<RefundRecord>) fetchResponse.getData();
        }
        return result;
	}
    

}
