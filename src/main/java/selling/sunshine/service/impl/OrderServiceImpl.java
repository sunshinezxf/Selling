package selling.sunshine.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import selling.sunshine.dao.OrderDao;
import selling.sunshine.model.Order;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.service.OrderService;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.Map;

public class OrderServiceImpl implements OrderService {
    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Override
    public ResultData placeOrder(Order order) {
        ResultData result = new ResultData();
        ResultData insertResponse = orderDao.insertOrder(order);
        result.setResponseCode(insertResponse.getResponseCode());
        if (insertResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(insertResponse.getData());
        } else {
            result.setDescription(insertResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchOrder(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData queryResponse = orderDao.queryOrder(condition);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchOrder(Map<String, Object> condition, MobilePageParam param) {
        ResultData result = new ResultData();
        ResultData queryResponse = orderDao.queryOrderByPage(condition, param);
        result.setResponseCode(queryResponse.getResponseCode());
        if (queryResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(queryResponse.getData());
        } else {
            result.setDescription(queryResponse.getDescription());
        }
        return result;
    }

	@Override
	public ResultData modifyOrder(Order order) {
		// TODO Auto-generated method stub
		return null;
	}
}
