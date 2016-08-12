package selling.sunshine.vo.cashback;

import selling.sunshine.model.lite.Agent;

/**
 * Created by sunshine on 8/10/16.
 */
public class CashBack4Agent {
    private Agent agent;

    private double amount;

    private double self;

    private double direct;

    private double indirect;

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getSelf() {
        return self;
    }

    public void setSelf(double self) {
        this.self = self;
    }

    public double getDirect() {
        return direct;
    }

    public void setDirect(double direct) {
        this.direct = direct;
    }

    public double getIndirect() {
        return indirect;
    }

    public void setIndirect(double indirect) {
        this.indirect = indirect;
    }
}
