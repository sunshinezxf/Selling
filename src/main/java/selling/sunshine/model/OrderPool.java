package selling.sunshine.model;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.model.selling.goods.Goods4Customer;
import common.sunshine.model.selling.agent.lite.Agent;

import java.sql.Date;

/**
 * 订单统计
 * @author wang_min
 *
 */
public class OrderPool extends Entity {

    private String orderPoolId;//ID
    private int quantity;//卖的数量
    private double price;//卖的金额
    private double refundAmount;//返现金额
    private Date poolDate;//生成Pool的时间
    private Agent agent;//代理商
    private Goods4Agent goods;//商品
    private RefundConfig refundConfig;//管理返现配置

    public OrderPool() {
        super();
        this.setBlockFlag(true);
    }

    public OrderPool(String orderPoolId, int quantity, double price, double refundAmount, Date poolDate, Agent agent, Goods4Customer goods, RefundConfig refundConfig) {
        this();
        this.orderPoolId = orderPoolId;
        this.quantity = quantity;
        this.price = price;
        this.refundAmount = refundAmount;
        this.poolDate = poolDate;
        this.agent = agent;
        this.goods = goods;
        this.refundConfig = refundConfig;
    }

    public String getOrderPoolId() {
        return orderPoolId;
    }

    public void setOrderPoolId(String orderPoolId) {
        this.orderPoolId = orderPoolId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Date getPoolDate() {
        return poolDate;
    }

    public void setPoolDate(Date poolDate) {
        this.poolDate = poolDate;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Goods4Agent getGoods() {
        return goods;
    }

    public void setGoods(Goods4Agent goods) {
        this.goods = goods;
    }

    public RefundConfig getRefundConfig() {
        return refundConfig;
    }

    public void setRefundConfig(RefundConfig refundConfig) {
        this.refundConfig = refundConfig;
    }


}
