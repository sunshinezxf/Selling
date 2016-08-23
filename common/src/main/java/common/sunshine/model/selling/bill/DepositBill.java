package common.sunshine.model.selling.bill;

import common.sunshine.model.selling.agent.lite.Agent;
import common.sunshine.model.selling.bill.Bill;
import common.sunshine.model.selling.bill.support.BillStatus;

/**
 * Created by sunshine on 5/10/16.
 */
public class DepositBill extends Bill {
    private Agent agent;

    public DepositBill() {
        super();
        agent = new Agent();
    }

    public DepositBill(double billAmount, String channel, String clientIp, Agent agent) {
        super(billAmount, channel, clientIp);
        this.agent = agent;
    }

    public DepositBill(double billAmount, String channel, String clientIp, BillStatus status, Agent agent) {
        super(billAmount, channel, clientIp, status);
        this.agent = agent;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
