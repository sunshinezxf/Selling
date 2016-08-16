package selling.sunshine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.model.Agent;
import selling.sunshine.model.cashback.CashBackLevel;
import selling.sunshine.model.cashback.CashBackRecord;
import selling.sunshine.pagination.DataTablePage;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.AgentService;
import selling.sunshine.service.CashBackService;
import selling.sunshine.service.RefundService;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import selling.sunshine.utils.ZipCompressor;
import selling.sunshine.vo.cashback.CashBack4Agent;
import selling.sunshine.vo.cashback.CashBack4AgentPerMonth;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sunshine on 8/12/16.
 */
@RestController
@RequestMapping("/cashback")
public class CashBackController {
    private Logger logger = LoggerFactory.getLogger(CashBackController.class);

    @Autowired
    private CashBackService cashBackService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private RefundService refundService;

    @RequestMapping(method = RequestMethod.GET, value = "/month")
    public ModelAndView monthly() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/refund/refund_record_month");
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

    @RequestMapping(method = RequestMethod.GET, value = "/{agentId}/month")
    public ModelAndView cashback(@PathVariable("agentId") String agentId) {
        ModelAndView view = new ModelAndView();
        Calendar current = Calendar.getInstance();
        current.add(Calendar.MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        view.addObject("month", format.format(current.getTime()));
        Map<String, Object> condition = new HashMap<>();
        condition.put("agentId", agentId);
        ResultData response = agentService.fetchAgent(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/cashback/month");
            return view;
        }
        Agent agent = ((List<Agent>) response.getData()).get(0);
        view.addObject("agent", agent);
        response = cashBackService.fetchCashBackPerMonth(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/cashback/month");
            return view;
        }
        CashBack4AgentPerMonth cashback = ((List<CashBack4AgentPerMonth>) response.getData()).get(0);
        view.addObject("cashback", cashback);
        condition.clear();
        condition.put("agentId", cashback.getAgent().getAgentId());
        condition.put("level", CashBackLevel.SELF.getCode());
        response = refundService.fetchRefundRecord(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/cashback/month");
            return view;
        }
        List<CashBackRecord> self = ((List<CashBackRecord>) response.getData());
        view.addObject("self", self);
        view.setViewName("/backend/refund/cashback_month_detail");
        return view;
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

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/produce")
    public ResultData produce() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        ResultData response = cashBackService.fetchCashBackPerMonth(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        List<CashBack4AgentPerMonth> list = (List<CashBack4AgentPerMonth>) response.getData();
        ResultData produceResponse = cashBackService.produce(list);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(calendar.getTime());
        String summaryPath = produceResponse.getData().toString();

        String path = DeliverController.class.getResource("/").getPath();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") >= 0) {
            path = path.substring(1);
        }
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/cashback";

        List<String> pathList = new ArrayList<String>();
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(date.replaceAll("-", ""));
        pathList.add(sb.toString());
        pathList.add((new StringBuffer(parent).append(summaryPath)).toString());
        String zipName = IDGenerator.generate("CashBack");
        StringBuffer sb2 = new StringBuffer(parent).append(directory).append("/").append(zipName + ".zip");
        ZipCompressor zipCompressor = new ZipCompressor(sb2.toString());
        zipCompressor.compress(pathList);

        return result;
    }
}
