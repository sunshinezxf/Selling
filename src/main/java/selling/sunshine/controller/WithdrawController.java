package selling.sunshine.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import common.sunshine.model.selling.admin.Admin;
import common.sunshine.model.selling.user.User;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import common.sunshine.utils.SortRule;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import selling.sunshine.form.TimeRangeForm;
import selling.sunshine.model.BackOperationLog;
import selling.sunshine.model.WithdrawRecord;
import selling.sunshine.model.WithdrawStatus;
import selling.sunshine.service.LogService;
import selling.sunshine.service.ToolService;
import selling.sunshine.service.WithdrawService;
import selling.sunshine.utils.ZipCompressor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提现controller类
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

    @Autowired
    private LogService logService;

    /**
     * 获取提现金额（pay：当前已申请提现但还未处理的金额；check：当前总申请提现金额）
     * @param status
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/money/{status}")
    public ResultData money(@PathVariable("status") String status) {
    	ResultData resultData=new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (status.equals("pay")) {
        	condition.put("pay", true);
		}else if (status.equals("check")) {
			condition.put("check", true);
		}        
        ResultData moneyResponse = withdrawService.fetchSumMoney(condition);
        if (moneyResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
        	resultData.setData(moneyResponse.getData());
        	return resultData;
        }
        resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
        return resultData;
    }

    /**
     * 跳转到提现列表界面
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        condition.put("check", true);
        ResultData moneyResponse = withdrawService.fetchSumMoney(condition);
        if (moneyResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            view.addObject("money", moneyResponse.getData());
        }
        view.setViewName("/backend/withdraw/overview");
        return view;
    }

    /**
     * 提现列表的datatable数据列表
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public DataTablePage<WithdrawRecord> overview(DataTableParam param) {
        DataTablePage<WithdrawRecord> result = new DataTablePage<WithdrawRecord>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        ResultData fetchResponse = withdrawService.fetchWithdrawRecord(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<WithdrawRecord>) fetchResponse.getData();
        }
        return result;
    }

    /**
     * 报表区查看提现信息的datatable接口（暂时未用到）
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/list")
    public DataTablePage<WithdrawRecord> list(DataTableParam param){
    	DataTablePage<WithdrawRecord> result = new DataTablePage<WithdrawRecord>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        List<Integer> status = new ArrayList<>();
        status.add(1);
        condition.put("status", status);
        condition.put("blockFlag", false);
        String params = param.getParams();
        if(!StringUtils.isEmpty(params)){
        	JSONObject jo = JSON.parseObject(params);
        	if(jo.containsKey("start") && jo.containsKey("end")){
        		condition.put("start", (String)jo.get("start"));
        		condition.put("end", (String)jo.get("end"));
        	}
        }
        ResultData fetchResponse = withdrawService.fetchWithdrawRecord(condition, param);
        if (fetchResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<WithdrawRecord>) fetchResponse.getData();
        }
        return result;
    }

    /**
     * 下载提现申请excel表格
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/check/download")
    public String download(HttpServletResponse response) throws UnsupportedEncodingException {
        Map<String, Object> condition = new HashMap<>();
        List<Integer> status = new ArrayList<>();
        status.add(0);
        condition.put("status", status);
        condition.put("blockFlag", true);
        ResultData result = withdrawService.fetchWithdrawRecord(condition);
        if (result.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return null;
        }
        List<WithdrawRecord> list = (List<WithdrawRecord>) result.getData();
        result = withdrawService.produceApply(list);
        if (result.getResponseCode() == ResponseCode.RESPONSE_OK) {
            response.setContentType("application/x-download;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("提现申请单.xlsx", "utf-8"));
            try {
                OutputStream out = response.getOutputStream();
                Workbook workbook = (Workbook) result.getData();
                workbook.write(out);
                out.flush();
                out.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * 下载提现单报表excel
     * @param fileName
     * @param tempFileName
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/download/{fileName}/{tempFileName}")
    public String downloadRecord(@PathVariable("fileName") String fileName, @PathVariable("tempFileName") String tempFileName, HttpServletRequest request,
                                 HttpServletResponse response) throws UnsupportedEncodingException {
        // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        // 2.设置文件头：最后一个参数是设置下载文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("提现单报表_" + fileName + ".zip", "utf-8"));
        OutputStream out;
        // 通过文件路径获得File对象
        String path = WithdrawController.class.getResource("/").getPath();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") >= 0) {
            path = path.substring(1);
        }
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/withdraw";
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(tempFileName + ".zip");
        File file = new File(sb.toString());
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream buff = new BufferedInputStream(fis);
            byte[] b = new byte[1024];// 相当于我们的缓存
            long k = 0;// 该值用于计算当前实际下载了多少字节

            // 3.通过response获取OutputStream对象(out)
            out = response.getOutputStream();
            // 开始循环下载
            while (k < file.length()) {
                int j = buff.read(b, 0, 1024);
                k += j;
                out.write(b, 0, j);
            }
            buff.close();
            fis.close();
            out.close();
            out.flush();
            file.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 提现单报表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/overviewreport")
    public ResultData withdraw() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        List<Integer> status = new ArrayList<>();
        status.add(1);
        condition.put("status", status);
        List<SortRule> rule = new ArrayList<>();
        rule.add(new SortRule("create_time", "asc"));
        condition.put("sort", rule);
        ResultData queryResponse = withdrawService.fetchWithdrawRecord(condition);
        Timestamp createAt = null;
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            WithdrawRecord record = ((List<WithdrawRecord>) queryResponse.getData()).get(0);
            createAt = record.getCreateAt();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("start", format.format(createAt));
            result.setData(jsonObject);
        }
        return result;
    }

    /**
     * 提现单报表
     * @param form
     * @param result
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/overviewreport")
    public ResultData withdraw(@Valid TimeRangeForm form, BindingResult result) {
        ResultData data = new ResultData();
        boolean empty = true;
        if (result.hasErrors()) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("start", form.getStart());
        condition.put("end", form.getEnd());
        List<Integer> status = new ArrayList<>();
        status.add(1);
        condition.put("status", status);
        condition.put("blockFlag", false);
        ResultData queryResponse = withdrawService.fetchWithdrawRecord(condition);
        List<WithdrawRecord> total = new ArrayList<WithdrawRecord>();
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            empty = false;
            List<WithdrawRecord> list = (List<WithdrawRecord>) queryResponse.getData();
            total.addAll(list);
            withdrawService.produceApply(list);
        }
        if (empty) {
            data.setResponseCode(ResponseCode.RESPONSE_NULL);
            return data;
        }

        ResultData resultData = withdrawService.produceSummary(total);
        String summaryPath = resultData.getData().toString();

        String path = WithdrawController.class.getResource("/").getPath();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") >= 0) {
            path = path.substring(1);
        }
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/withdraw";
        List<String> pathList = new ArrayList<String>();
        pathList.add((new StringBuffer(parent).append(summaryPath)).toString());
        String zipName = IDGenerator.generate("Withdraw");
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(zipName + ".zip");
        ZipCompressor zipCompressor = new ZipCompressor(sb.toString());
        zipCompressor.compress(pathList);
        File file = new File((new StringBuffer(parent).append(summaryPath)).toString());
        file.delete();
        data.setData(zipName);
        return data;
    }


    /**
     * 用户发起提现，系统转账成功通知
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
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

    /**
     * 确认提现申请
     * @param withdrawId
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{withdrawId}/send")
    public ModelAndView send(@PathVariable("withdrawId") String withdrawId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        condition.put("withdrawId", withdrawId);
        ResultData fetchResponse = withdrawService.fetchWithdrawRecord(condition);
        if (fetchResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            view.setViewName("redirect:/withdraw/overview");
            return view;
        }
        WithdrawRecord record = ((List<WithdrawRecord>) fetchResponse.getData()).get(0);
        if (record.isBlockFlag() == true) {
            record.setBlockFlag(false);
            ResultData updateResponse = withdrawService.updateWithdrawRecord(record);
            if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                Subject subject = SecurityUtils.getSubject();
                User user = (User) subject.getPrincipal();
                if (user == null) {
                    view.setViewName("redirect:/withdraw/overview");
                    return view;
                }
                Admin admin = user.getAdmin();
                BackOperationLog backOperationLog = new BackOperationLog(
                        admin.getUsername(), toolService.getIP(request), "管理员" + admin.getUsername() + "确认了提现申请: 提现代理商为" + record.getAgent().getName() + ", 提现金额为" + record.getAmount());
                logService.createbackOperationLog(backOperationLog);
                view.setViewName("redirect:/withdraw/overview");
                return view;
            }
        }
        view.setViewName("redirect:/withdraw/overview");
        return view;
    }
}
