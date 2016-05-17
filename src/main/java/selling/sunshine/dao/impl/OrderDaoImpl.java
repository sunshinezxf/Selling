package selling.sunshine.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.OrderDao;
import selling.sunshine.model.Order;
import selling.sunshine.model.OrderItem;
import selling.sunshine.pagination.MobilePage;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDaoImpl extends BaseDao implements OrderDao {
    private Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

    private Object lock = new Object();

    @Transactional
    @Override
    public ResultData insertOrder(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                order.setOrderId(IDGenerator.generate("ODR"));
                sqlSession.insert("selling.order.insert", order);
                for (OrderItem orderItem : orderItems) {
                    orderItem.setOrderItemId(IDGenerator.generate("ORI"));
                    orderItem.setOrder(order);
                }
                sqlSession.insert("selling.order.item.insertBatch", orderItems);
                order.setOrderItems(orderItems);
                result.setData(order);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result = insertOrder(order);
            } finally {
                return result;
            }
        }
    }

    @Override
    public ResultData queryOrder(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
        	condition = handle(condition);
            List<Order> list = sqlSession.selectList("selling.order.query", condition);
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
    public ResultData queryOrderByPage(Map<String, Object> condition, MobilePageParam param) {
        ResultData result = new ResultData();
        MobilePage<Order> page = new MobilePage<>();
        ResultData total = queryOrder(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setTotal(((List<Order>) total.getData()).size());
        List<Order> current = queryOrderByPage(condition, param.getStart(), param.getLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    @Transactional
    @Override
    public ResultData updateOrder(Order order) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.order.update", order);
                Map<String, Object> condition = new HashMap<>();
                condition.put("orderId", order.getOrderId());
                Order target = sqlSession.selectOne("selling.order.query", condition);
                if (target == null) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("Order does not exist.");
                }
                List<OrderItem> primary = target.getOrderItems();
                List<OrderItem> now = order.getOrderItems();
                List<OrderItem> toDelete = new ArrayList<>();
                if (primary.size() == 0) {
                    for (OrderItem item : now) {
                        item.setOrderItemId(IDGenerator.generate("ORI"));
                    }
                    sqlSession.insert("selling.order.item.insertBatch", now);
                }
                if (now.size() == 0) {
                    toDelete.addAll(primary);
                    sqlSession.delete("selling.orderItem.delete", toDelete);
                }
                OrderItem primaryItem = primary.get(0);
                OrderItem nowItem = now.get(0);
                for (int i = 0; i < now.size(); i++) {
                    if (StringUtils.isEmpty(nowItem.getOrderItemId())) {
                        toDelete.addAll(primary);
                        sqlSession.delete("selling.order.item.delete", toDelete);
                        sqlSession.insert("selling.order.item.insert", now);
                    }
                    if (primaryItem.getOrderItemId().equals(nowItem.getOrderItemId())) {
                        sqlSession.update("selling.order.item.update", nowItem);
                        primary.remove(primaryItem);
                        now.remove(nowItem);
                        primaryItem = primary.get(0);
                        nowItem = now.get(0);
                        i--;
                        continue;
                    }
                    toDelete.add(primaryItem);
                    primary.remove(primaryItem);
                    primaryItem = primary.get(0);
                }
                sqlSession.delete("selling.order.item.delete", toDelete);
                sqlSession.insert("selling.order.item.insertBatch", now);
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

    private List<Order> queryOrderByPage(Map<String, Object> condition, int start, int length) {
        List<Order> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.order.query", condition, new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }
}
