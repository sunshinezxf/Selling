package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selling.sunshine.dao.CashBackDao;
import selling.sunshine.pagination.DataTableParam;
import selling.sunshine.service.CashBackService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.Map;

/**
 * Created by sunshine on 8/12/16.
 */
@Service
public class CashBackServiceImpl implements CashBackService {
    private Logger logger = LoggerFactory.getLogger(CashBackServiceImpl.class);

    @Autowired
    private CashBackDao cashBackDao;

    @Override
    public ResultData fetchCashBackPerMonth(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData response = cashBackDao.queryMonthlyByPage(condition, param);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchCashBack(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData response = cashBackDao.queryByPage(condition, param);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }
}
