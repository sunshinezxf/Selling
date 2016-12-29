package selling.sunshine.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.utils.IDGenerator;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import common.sunshine.dao.BaseDao;
import selling.sunshine.dao.CustomerOrderDao;
import common.sunshine.model.selling.order.CustomerOrder;
import selling.sunshine.model.OrderPool;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.pagination.MobilePage;
import common.sunshine.pagination.MobilePageParam;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunshine on 6/14/16.
 */
@Repository
public class CustomerOrderDaoImpl extends BaseDao implements CustomerOrderDao {
    private Logger logger = LoggerFactory.getLogger(CustomerOrderDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertOrder(CustomerOrder order) {
        ResultData result = new ResultData();
        order.setOrderId(IDGenerator.generate("CUO"));
        synchronized (lock) {
            try {
                sqlSession.insert("selling.customer.order.insert", order);
                result.setData(order);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    @Override
    public ResultData updateOrder(CustomerOrder customerOrder) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.customer.order.update", customerOrder);
                result.setData(customerOrder);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                return result;
            }
        }
    }

    @Override
    public ResultData queryOrder(Map<String, Object> condition) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                List<OrderPool> list = sqlSession.selectList("selling.customer.order.query", condition);
                result.setData(list);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    @Override
    public ResultData queryOrderByPage(Map<String, Object> condition, MobilePageParam param) {
        ResultData result = new ResultData();
        MobilePage<CustomerOrder> page = new MobilePage<>();
        ResultData total = queryOrder(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setTotal(((List) total.getData()).size());
        List<CustomerOrder> current = queryCustomerOrderByPage(condition, param.getStart(), param.getLength());
        if (current.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    @Override
    public ResultData queryOrderByPage(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<CustomerOrder> page = new DataTablePage<>();
        condition = handle(condition);
        if (!StringUtils.isEmpty(param.getsSearch())) {
            String searchParam = param.getsSearch().replace("/", "-");
            condition.put("search", "%" + searchParam + "%");
        }
        if (!StringUtils.isEmpty(param.getParams())) {
            JSONObject json = JSON.parseObject(param.getParams());
            if (json.containsKey("status")) {
                List<Integer> status = new ArrayList<>();
                switch (json.getString("status")) {
                    case "PAYED":
                        condition.put("status", status.add(OrderItemStatus.PAYED.getCode()));
                        break;
                    case "NOT_PAYED":
                        status.add(OrderItemStatus.NOT_PAYED.getCode());
                        condition.put("status", status);
                        break;
                    case "SENT":
                        status.add(OrderItemStatus.SHIPPED.getCode());
                        condition.put("status", status);
                        break;
                    case "RECEIVED":
                        status.add(OrderItemStatus.RECEIVED.getCode());
                        condition.put("status", status);
                        break;
                    case "REFUNDING":
                        status.add(OrderItemStatus.REFUNDING.getCode());
                        condition.put("status", status);
                        break;
                    case "REFUNDED":
                        status.add(OrderItemStatus.REFUNDED.getCode());
                        condition.put("status", status);
                        break;
                }
            }
            if (json.containsKey("start")) {
                condition.put("start", json.get("start"));
            }
            if (json.containsKey("end")) {
                condition.put("end", json.get("end"));
            }
        }
        ResultData total = queryOrder(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List) total.getData()).size());
        page.setiTotalDisplayRecords(((List) total.getData()).size());
        List<CustomerOrder> current = queryCustomerOrderByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    private List<CustomerOrder> queryCustomerOrderByPage(Map<String, Object> condition, int start, int length) {
        List<CustomerOrder> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.customer.order.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }
}
