package selling.sunshine.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.sunshine.dao.BaseDao;
import common.sunshine.model.selling.order.OrderItem;
import common.sunshine.model.selling.order.support.OrderItemStatus;
import common.sunshine.pagination.DataTablePage;
import common.sunshine.pagination.DataTableParam;
import common.sunshine.utils.IDGenerator;
import common.sunshine.utils.ResponseCode;
import common.sunshine.utils.ResultData;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import selling.sunshine.dao.OrderItemDao;
import selling.sunshine.vo.order.OrderItemSum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class OrderItemDaoImpl extends BaseDao implements OrderItemDao {
    private Logger logger = LoggerFactory.getLogger(OrderItemDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData insertOrderItems(List<OrderItem> orderItems) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                for (OrderItem orderItem : orderItems) {
                    orderItem.setOrderItemId(IDGenerator.generate("ODR"));
                }
                sqlSession.insert("selling.order.insertBatch", orderItems);
                result.setData(orderItems);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result = insertOrderItems(orderItems);
            } finally {
                return result;
            }
        }
    }

    @Override
    public ResultData queryOrderItem(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<OrderItem> list = sqlSession.selectList("selling.order.item.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData queryOrderItemSum(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            condition = handle(condition);
            List<OrderItemSum> list = sqlSession.selectList("selling.order.orderitemsum.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData queryOrderItemSum(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        DataTablePage<OrderItemSum> page = new DataTablePage<>(param);
        condition = handle(condition);
        if (!StringUtils.isEmpty(param.getsSearch()) && !StringUtils.isEmpty(param.getsSearch().trim())) {
            String search = param.getsSearch().replaceAll("/", "-");
            condition.put("search", "%" + search + "%");
        }
        if (!StringUtils.isEmpty(param.getParams())) {
            JSONObject json = JSON.parseObject(param.getParams());
            if (json.containsKey("status")) {
                switch (json.getString("status")) {
                    case "PAYED":
                        condition.put("status", OrderItemStatus.PAYED.getCode());
                        break;
                    case "NOT_PAYED":
                        condition.put("status", OrderItemStatus.NOT_PAYED.getCode());
                        break;
                    case "SENT":
                        condition.put("status", OrderItemStatus.SHIPPED.getCode());
                        break;
                    case "RECEIVED":
                        condition.put("status", OrderItemStatus.RECEIVED.getCode());
                        break;
                    case "REFUNDING":
                        condition.put("status", OrderItemStatus.REFUNDING.getCode());
                        break;
                    case "REFUNDED":
                        condition.put("status", OrderItemStatus.REFUNDED.getCode());
                        break;
                }
            }
        }
        ResultData total = queryOrderItemSum(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setiTotalRecords(((List<OrderItemSum>) total.getData()).size());
        page.setiTotalDisplayRecords(((List<OrderItemSum>) total.getData()).size());
        List<OrderItemSum> current = queryOrderItemSumByPage(condition, param.getiDisplayStart(), param.getiDisplayLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    @Override
    public ResultData updateOrderItem(OrderItem orderItem) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.order.item.update", orderItem);
                result.setData(orderItem);
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

    private List<OrderItemSum> queryOrderItemSumByPage(Map<String, Object> condition, int start, int length) {
        List<OrderItemSum> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.order.orderitemsum.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }
}
