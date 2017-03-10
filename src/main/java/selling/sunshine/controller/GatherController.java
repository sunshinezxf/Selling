package selling.sunshine.controller;

import common.sunshine.model.selling.bill.RefundBill;
import common.sunshine.utils.IDGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import selling.sunshine.form.TimeRangeForm;
import common.sunshine.model.selling.bill.CustomerOrderBill;
import common.sunshine.model.selling.bill.DepositBill;
import common.sunshine.model.selling.bill.OrderBill;
import common.sunshine.model.selling.bill.support.BillStatus;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import selling.sunshine.service.BillService;
import selling.sunshine.service.GatherService;
import selling.sunshine.utils.DateUtils;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import selling.sunshine.utils.ZipCompressor;
import selling.sunshine.vo.bill.BillSumVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 7/12/16.
 */
@RequestMapping("/gather")
@RestController
public class GatherController {
    private Logger logger = LoggerFactory.getLogger(GatherController.class);

    @Autowired
    private BillService billService;

    @Autowired
    private GatherService gatherService;
    
    /**
     * 后台报表下载-收款单页面
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ResultData gather() {
    	ResultData result = new ResultData();
        return result;
    }

    /**
     * 后台收款单下载表单，需要提供一个时间段
     * @param form
     * @param result
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/overview")
    public ResultData gather(@Valid TimeRangeForm form, BindingResult result) {
        ResultData data = new ResultData();
        boolean empty = true;
        if (result.hasErrors()) {
            data.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return data;
        }
        List total = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("start", form.getStart());
        condition.put("end", form.getEnd());
        condition.put("status", 1);
        ResultData queryResponse = billService.fetchOrderBill(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            empty = false;
            List<OrderBill> list = (List<OrderBill>) queryResponse.getData();
            total.addAll(list);
            gatherService.produce(list);
        }
        queryResponse = billService.fetchCustomerOrderBill(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            empty = false;
            List<CustomerOrderBill> list = ((List<CustomerOrderBill>) queryResponse.getData());
            total.addAll(list);
            gatherService.produce(list);
        }
        queryResponse = billService.fetchDepositBill(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            empty = false;
            List<DepositBill> list = ((List<DepositBill>) queryResponse.getData());
            total.addAll(list);
            gatherService.produce(list);
        }
        //查询退款单
        queryResponse = billService.fetchRefundBill(condition);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK){
            empty=false;
            List<RefundBill> list = ((List<RefundBill>) queryResponse.getData());
            total.addAll(list);
        }

        if (empty) {
            data.setResponseCode(ResponseCode.RESPONSE_NULL);
            return data;
        }

        ResultData resultData = gatherService.produceSummary(total);
        String summaryPath = resultData.getData().toString();

        String path = GatherController.class.getResource("/").getPath();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") >= 0) {
            path = path.substring(1);
        }

        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/gather";
        DateUtils dateUtils = new DateUtils();
        dateUtils.process(form.getStart(), form.getEnd());
        List<String> dateList = dateUtils.getDateList();
        List<String> pathList = new ArrayList<String>();
        dateList.forEach((date) -> {
            StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(date.replaceAll("-", ""));
            pathList.add(sb.toString());
        });
        pathList.add((new StringBuffer(parent).append(summaryPath)).toString());
        String zipName = IDGenerator.generate("Gather");
        StringBuffer sb = new StringBuffer(parent).append(directory).append("/").append(zipName + ".zip");
        ZipCompressor zipCompressor = new ZipCompressor(sb.toString());
        zipCompressor.compress(pathList);
        File file = new File((new StringBuffer(parent).append(summaryPath)).toString());
        file.delete();
        data.setData(zipName);

        return data;
    }

    /**
     * 后台收款单下载
     * @param fileName
     * @param tempFileName
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/download/{fileName}/{tempFileName}")
    public String download(@PathVariable("fileName") String fileName, @PathVariable("tempFileName") String tempFileName, HttpServletRequest request,
                           HttpServletResponse response) throws UnsupportedEncodingException {
        // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        // 2.设置文件头：最后一个参数是设置下载文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("收款单报表_" + fileName + ".zip", "utf-8"));
        OutputStream out;
        // 通过文件路径获得File对象
        String path = IndentController.class.getResource("/").getPath();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") >= 0) {
            path = path.substring(1);
        }
        int index = path.lastIndexOf("/WEB-INF/classes/");
        String parent = path.substring(0, index);
        String directory = "/material/journal/gather";
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
     * 后台获取账单信息列表
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/list")
    public DataTablePage<BillSumVo> list(DataTableParam param) {
        DataTablePage<BillSumVo> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("status", BillStatus.PAYED.getCode());
        ResultData queryResponse = billService.fetchBillSum(condition, param);
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<BillSumVo>) queryResponse.getData();
        }
        return result;
    }
}
