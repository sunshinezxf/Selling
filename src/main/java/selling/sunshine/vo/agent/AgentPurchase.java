package selling.sunshine.vo.agent;

import common.sunshine.model.selling.agent.lite.Agent;

/**
 * Created by sunshine on 2016/11/14.
 */
public class AgentPurchase {
    private Agent agent;

    private int quantity;

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
