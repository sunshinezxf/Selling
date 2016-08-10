package selling.sunshine.service.impl;

import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Transfer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import selling.sunshine.dao.WithdrawDao;
import selling.sunshine.model.WithdrawRecord;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.WithdrawService;
import selling.sunshine.utils.PlatformConfig;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;
import selling.sunshine.utils.WorkBookUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithdrawServiceImpl implements WithdrawService {

    private Logger logger = LoggerFactory.getLogger(WithdrawServiceImpl.class);

    {
        Pingpp.apiKey = PlatformConfig.getValue("pingxx_api_key");
    }

    @Autowired
    private WithdrawDao withdrawDao;

    @Override
    public ResultData createWithdrawRecord(WithdrawRecord record) {
        ResultData result = new ResultData();
        Map<String, Object> params = new HashMap<>();
        params.put("channel", "wx_pub");//此处 wx_pub 为公众平台的支付
        params.put("order_no", record.getWithdrawId());
        params.put("amount", record.getAmount() * 100);//订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
        params.put("type", "b2c");
        params.put("currency", "cny");
        params.put("recipient", record.getOpenId());//企业转账给指定用户的 open_id
        params.put("description", "代理商提现");
        Map<String, String> app = new HashMap<>();
        app.put("id", PlatformConfig.getValue("pingxx_app_id"));
        params.put("app", app);
        Map<String, Object> extra = new HashMap<>();
        extra.put("user_name", "");
        extra.put("force_check", false);
        params.put("extra", extra);
        try {
            Transfer.create(params);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData fetchWithdrawRecord(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = withdrawDao.queryWithdrawByPage(condition, param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchWithdrawRecord(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = withdrawDao.queryWithdraw(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List) queryResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData updateWithdrawRecord(WithdrawRecord record) {
        ResultData result = new ResultData();
        ResultData updateResponse = withdrawDao.updateWithdraw(record);
        result.setResponseCode(updateResponse.getResponseCode());
        if (updateResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(updateResponse.getData());
        } else {
            result.setDescription(updateResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData statistic(Map<String, Object> condition) {
        ResultData result = new ResultData();
        result = withdrawDao.statistic(condition);
        if (result.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List) result.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
        }
        return result;
    }

    @Override
    public ResultData fetchSumMoney(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData sumResponse = withdrawDao.money(condition);
        result.setResponseCode(sumResponse.getResponseCode());
        if (sumResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            double money = 0;
            if (!StringUtils.isEmpty(sumResponse.getData())) {
                money = (double) sumResponse.getData();
            }
            result.setData(money);
        }
        return result;
    }

    @Override
    public ResultData produceApply(List<WithdrawRecord> list) {
        ResultData result = new ResultData();
        Workbook workbook = WorkBookUtil.getWithdrawApplyTemplate();
        int index = 3;
        double sum = 0;
        Sheet sheet = workbook.getSheetAt(0);
        for (WithdrawRecord item : list) {
            sum += item.getAmount();
            Row current = sheet.createRow(index);
            Cell withdrawNo = current.createCell(0);
            withdrawNo.setCellValue(item.getWithdrawId());
            Cell agentName = current.createCell(1);
            agentName.setCellValue(item.getAgent().getName());
            Cell amount = current.createCell(2);
            amount.setCellValue(item.getAmount());
            Cell createAt = current.createCell(3);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            createAt.setCellValue(format.format(item.getCreateAt()));
            Cell channel = current.createCell(4);
            channel.setCellValue("wx_pub");
            Cell account = current.createCell(5);
            account.setCellValue(item.getOpenId());
            index ++;
        }
        Row row = sheet.getRow(1);
        Cell total = row.createCell(2);
        total.setCellValue(sum);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar instance = Calendar.getInstance();
        Cell time = row.createCell(5);
        time.setCellValue(format.format(instance.getTime()));
        result.setData(workbook);
        return result;
    }
}
