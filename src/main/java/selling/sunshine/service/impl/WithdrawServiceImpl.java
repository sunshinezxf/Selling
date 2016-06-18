package selling.sunshine.service.impl;

import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.dao.WithdrawDao;
import selling.sunshine.model.WithdrawRecord;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.WithdrawService;
import selling.sunshine.utils.PlatformConfig;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
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
        params.put("amount", record.getAmount());//订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
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
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData updateWithdrawRecord(Map<String, Object> condition) {
        ResultData result = new ResultData();

        return result;
    }
}
