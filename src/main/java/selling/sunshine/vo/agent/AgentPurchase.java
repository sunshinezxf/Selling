package selling.sunshine.vo.agent;

import common.sunshine.model.selling.agent.lite.Agent;

/**
 * 代理商购买商品数量类
 * Created by sunshine on 2016/11/14.
 */
public class AgentPurchase {
    private Agent agent;//关联代理商

    private int quantity;//购买商品数量

    public AgentPurchase() {
        super();
    }

    public AgentPurchase(Agent agent, int quantity) {
        this();
        this.agent = agent;
        this.quantity = quantity;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
