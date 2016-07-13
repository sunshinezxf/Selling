package selling.sunshine.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.dao.ExpressDao;
import selling.sunshine.model.express.Express;
import selling.sunshine.model.express.Express4Agent;
import selling.sunshine.model.express.Express4Customer;
import selling.sunshine.service.ExpressService;
import selling.sunshine.utils.DigestUtil;
import selling.sunshine.utils.HttpUtil;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressServiceImpl implements ExpressService {

    private Logger logger = LoggerFactory.getLogger(ExpressServiceImpl.class);

    @Autowired
    private ExpressDao expressDao;

    @Override
    public ResultData createExpress(Express4Agent express) {
        ResultData result = new ResultData();
        ResultData insertResponse = expressDao.insertExpress4Agent(express);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData createExpress(Express4Customer express) {
        ResultData result = new ResultData();
        ResultData insertResponse = expressDao.insertExpress4Customer(express);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchExpress(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = expressDao.queryExpress(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Express>) queryResponse.getData()).size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchExpress4Agent(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = expressDao.queryExpress4Agent(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Express4Agent>) queryResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchExpress4Customer(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = expressDao.queryExpress4Customer(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            if (((List<Express4Customer>) queryResponse.getData()).isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(queryResponse.getData());
        } else if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData traceExpress(String expressNo, String type) {
        ResultData result = new ResultData();
        Map<String, String> map = new HashMap<String, String>();
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(expressNo);
        String data = jsonArray.toJSONString();
        map.put("data", data);
        map.put("msg_type", type);
        try {
            map.put("data_digest", DigestUtil.digest(data, "AA076973A63D4CD2BBEFB60544FC1262", DigestUtil.UTF8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("company_id", "5be4f9cacac84d3d9dace29dd9026a09");
        try {
            result.setData(HttpUtil.post("http://japi.zto.cn/zto/api_utf8/traceInterface", "UTF-8", map));
        } catch (IOException e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            e.printStackTrace();
        } finally {
            return result;
        }
    }

	@Override
	public ResultData receiveCheck() {
		ResultData result = new ResultData();
		//查询已发货的OrderItem
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("status", "");
		return result;
	}
}
