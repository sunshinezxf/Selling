package selling.sunshine.dao.impl;

import org.apache.ibatis.session.RowBounds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import selling.sunshine.dao.BaseDao;
import selling.sunshine.dao.OrderDao;
import selling.sunshine.model.*;
import selling.sunshine.pagination.MobilePage;
import selling.sunshine.pagination.MobilePageParam;
import selling.sunshine.utils.IDGenerator;
import selling.sunshine.utils.ResponseCode;
import selling.sunshine.utils.ResultData;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderDaoImpl extends BaseDao implements OrderDao {
    private Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

    private Object lock = new Object();

    /**
     * 添加订单,在order表中添加记录,并将所有的订单项添加到order item中
     *
     * @param order
     * @return
     */
    @Transactional
    @Override
    public ResultData insertOrder(Order order) {
        ResultData result = new ResultData();
        List<OrderItem> orderItems = order.getOrderItems();
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
                result.setDescription(e.getMessage());
            } finally {
                return result;
            }
        }
    }

    /**
     * 查询符合查询条件的订单列表
     *
     * @param condition
     * @return
     */
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
    public ResultData queryOrder2(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            condition = handle(condition);
            List<Order> list = sqlSession.selectList("selling.order.queryOrder",
                    condition);
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
        List<Order> current = queryOrderByPage(condition, param.getStart(),
                param.getLength());
        if (current.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        page.setData(current);
        result.setData(page);
        return result;
    }

    @Override
    public ResultData queryOrderByPage2(Map<String, Object> condition, MobilePageParam param) {
        ResultData result = new ResultData();
        MobilePage<Order> page = new MobilePage<>();
        ResultData total = queryOrder2(condition);
        if (total.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(total.getDescription());
            return result;
        }
        page.setTotal(((List<Order>) total.getData()).size());
        List<Order> current = queryOrderByPage2(condition, param.getStart(),
                param.getLength());
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
                List<OrderItem> toDelete = new ArrayList<OrderItem>();
                List<OrderItem> toAdd = new ArrayList<OrderItem>();
                List<OrderItem> toModify = new ArrayList<OrderItem>();
                if (primary.size() == 0) {
                    for (OrderItem item : now) {
                        item.setOrderItemId(IDGenerator.generate("ORI"));
                        Order temp = new Order();
                        temp.setOrderId(order.getOrderId());
                        item.setOrder(temp);
                    }
                    sqlSession.insert("selling.order.item.insertBatch", now);
                }
                if (now.size() == 0) {
                    toDelete.addAll(primary);
                    sqlSession.delete("selling.order.item.delete", toDelete);
                }
                if (primary.size() != 0 && now.size() != 0) {
                    for (OrderItem nowItem : now) {
                        boolean isMatch = false;
                        for (OrderItem primaryItem : primary) {
                            if (nowItem.getOrderItemId().equals(primaryItem.getOrderItemId())) {
                                toModify.add(nowItem);
                                primary.remove(primaryItem);
                                isMatch = true;
                                break;
                            }
                        }
                        if (!isMatch) {
                            toAdd.add(nowItem);
                        }
                    }
                }
                toDelete.addAll(primary);
                if (toDelete.size() > 0) {
                    sqlSession.delete("selling.order.item.delete", toDelete);
                }
                if (toAdd.size() > 0) {
                    for (OrderItem item : toAdd) {
                        item.setOrderItemId(IDGenerator.generate("ORI"));
                        Order temp = new Order();
                        temp.setOrderId(order.getOrderId());
                        item.setOrder(temp);
                    }
                    sqlSession.insert("selling.order.item.insertBatch", toAdd);
                }
                if (toModify.size() > 0) {
                    for (OrderItem item : toModify) {
                        sqlSession.update("selling.order.item.update", item);
                    }
                }
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

    private List<Order> queryOrderByPage(Map<String, Object> condition,
                                         int start, int length) {
        List<Order> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.order.query", condition,
                    new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

    private List<Order> queryOrderByPage2(Map<String, Object> condition,
                                          int start, int length) {
        List<Order> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("selling.order.queryOrder", condition,
                    new RowBounds(start, length));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            return result;
        }
    }

    @Override
    public ResultData cancelOrder(Order order) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("selling.order.update", order);
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
    public ResultData sumOrder() {
        ResultData result = new ResultData();
        // 获取当月的前一个月的日期 xxxx年xx月
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Timestamp lastMonth = new Timestamp(c.getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String date = dateFormat.format(lastMonth);
        try {
            Map<String, Object> condition = new HashMap<>();
            condition.put("date", date + "%");
            List<Map<String, Object>> resultList = sqlSession.selectList(
                    "selling.order.pool.sumOrder", condition);
            Map<String, Object> configCondition = new HashMap<>();
            List<RefundConfig> configList = sqlSession.selectList("selling.refund.config.query", configCondition);
            for (int i = 0; i < resultList.size(); i++) {
                OrderPool pool = new OrderPool();
                pool.setOrderPoolId(IDGenerator.generate("OPL"));
                pool.setPrice(Double.parseDouble(resultList.get(i).get("price")
                        .toString()));
                pool.setPoolDate(new Date(c.getTimeInMillis()));
                Agent agent = new Agent();
                agent.setAgentId((String) resultList.get(i).get("agent"));
                pool.setAgent(agent);
                Goods goods = new Goods();
                goods.setGoodsId((String) resultList.get(i).get("goods"));
                pool.setGoods(goods);
                Map<String, Object> customerOrderCon = new HashMap<>();
                customerOrderCon.put("agentId", resultList.get(i).get("agent"));
                customerOrderCon.put("goodsId", resultList.get(i).get("goods"));
                customerOrderCon.put("date", date + "%");
                if (sqlSession.selectList("selling.refund.config.query",customerOrderCon).size()!=0) {
                    CustomerOrder customerOrder=(CustomerOrder)sqlSession.selectList("selling.refund.config.query",customerOrderCon).get(0);
                    pool.setQuantity(Integer.parseInt(resultList.get(i)
                            .get("quantity").toString())+customerOrder.getQuantity());
				}else {
					 pool.setQuantity(Integer.parseInt(resultList.get(i)
	                            .get("quantity").toString()));
				}
                for (RefundConfig config : configList) {
                    if (config.getGoods().getGoodsId().equals((String) resultList.get(i).get("goods"))) {
                        if (pool.getQuantity()>= config.getAmountTrigger()) {
                        	pool.setRefundConfig(config);
                            pool.setBlockFlag(false);
                            Map<String, Object> level1Con = new HashMap<>();
                            level1Con.put("agentId", (String) resultList.get(i).get("agent"));
                            Agent agentLevel1 = (Agent) sqlSession.selectList("selling.agent.query", level1Con).get(0);
                            if (agentLevel1.getUpperAgent() != null) {
                                Map<String, Object> level2Con = new HashMap<>();
                                level2Con.put("agentId", agentLevel1.getUpperAgent().getAgentId());
                                Agent agentLevel2 = (Agent) sqlSession.selectList("selling.agent.query", level2Con).get(0);
                                if (agentLevel2.getUpperAgent() != null) {
                                    Map<String, Object> level3Con = new HashMap<>();
                                    level2Con.put("agentId", agentLevel2.getUpperAgent().getAgentId());
                                    Agent agentLevel3 = (Agent) sqlSession.selectList("selling.agent.query", level3Con).get(0);
                                    //当前agent为三级代理商
                                    pool.setRefundAmount(Double.parseDouble(resultList.get(i).get("quantity").toString()) * config.getLevel3Percent());

                                } else {
                                    //当前agent为二级代理商
                                    pool.setRefundAmount(Double.parseDouble(resultList.get(i).get("quantity").toString()) * config.getLevel2Percent());
                                }
                            } else {
                                //当前agent为一级代理商
                                pool.setRefundAmount(Double.parseDouble(resultList.get(i).get("quantity").toString()) * config.getLevel1Percent());
                            }

                        }
                    }
                }
                sqlSession.insert("selling.order.pool.insert", pool);
            }
            result.setData(resultList);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }

    }

}
