package selling.sunshine.service.impl;

import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Charge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import selling.sunshine.dao.BillDao;
import selling.sunshine.model.DepositBill;
import selling.sunshine.service.BillService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunshine on 5/10/16.
 */
@Service
public class BillServiceImpl implements BillService {

    private Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

    {
        Pingpp.apiKey = "sk_test_Xf5mr95mrz5S8yLCG040mvrH";
    }

    @Autowired
    private BillDao billDao;

    @Override
    public ResultData createDepositBill(DepositBill bill) {
        ResultData result = new ResultData();
        ResultData insertResponse = billDao.insertDepositBill(bill);
        if (insertResponse.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(insertResponse.getDescription());
        }
        bill = (DepositBill) insertResponse.getData();
        Map<String, Object> params = new HashMap<>();
        params.put("order_no", bill.getBillId());
        params.put("amount", bill.getBillAmount());
        Map<String, Object> app = new HashMap<>();
        app.put("id", "app_DazjbTLybjHGbv9O");
        params.put("app", app);
        params.put("channel", bill.getChannel());
        if (!StringUtils.isEmpty(bill.getChannel()) && bill.getChannel().equals("alipay_wap")) {
            Map<String, String> url = new HashMap<>();
            url.put("success_url", "http://yuncaogangmu.com");
            url.put("cancel_url", "http://yuncaogangmu.com");
            params.put("extra", url);
        }
        params.put("currency", "cny");
        params.put("client_ip", bill.getClientIp());
        params.put("subject", "代理商账户充值");
        params.put("body", "充值金额为:" + bill.getBillAmount() + "元");
        try {
            Charge charge = Charge.create(params);
            result.setData(charge);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
