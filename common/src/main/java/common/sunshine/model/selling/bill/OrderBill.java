package common.sunshine.model.selling.bill;

import common.sunshine.model.selling.agent.lite.Agent;
import common.sunshine.model.selling.bill.support.BillStatus;
import common.sunshine.model.selling.order.Order;

/**
 * 该类为代理商订单账单类
 * Created by sunshine on 5/10/16.
 */
public class OrderBill extends Bill {

    /* 账单对应的代理商 */
    private Agent agent;

    /* 账单对应的订单 */
    private Order order;

    public OrderBill() {
        super();
        this.agent = new Agent();
        this.order = new Order();
    }

    public OrderBill(double billAmount, String channel, String clientIp, Agent agent, Order order) {
        super(billAmount, channel, clientIp);
        this.agent = agent;
        this.order = order;
    }

    public OrderBill(double billAmount, String channel, String clientIp, BillStatus status, Agent agent, Order order) {
        super(billAmount, channel, clientIp, status);
        this.agent = agent;
        this.order = order;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


}
