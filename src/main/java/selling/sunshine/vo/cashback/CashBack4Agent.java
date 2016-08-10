package selling.sunshine.vo.cashback;

import selling.sunshine.model.cashback.CashBackLevel;
import selling.sunshine.model.lite.Agent;

/**
 * Created by sunshine on 8/10/16.
 */
public class CashBack4Agent {
    private Agent agent;
    private double amount;
    private CashBackLevel level;

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

    public CashBackLevel getLevel() {
        return level;
    }

    public void setLevel(CashBackLevel level) {
        this.level = level;
    }
}
