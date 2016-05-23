package selling.sunshine.model;

/**
 * Created by sunshine on 5/10/16.
 */
public class DepositBill extends Bill {
    private selling.sunshine.model.lite.Agent agent;

    public DepositBill() {
        super();
        agent = new selling.sunshine.model.lite.Agent();
    }

    public DepositBill(double billAmount, String channel, String clientIp, selling.sunshine.model.lite.Agent agent) {
        super(billAmount, channel, clientIp);
        this.agent = agent;
    }

    public DepositBill(double billAmount, String channel, String clientIp, BillStatus status, selling.sunshine.model.lite.Agent agent) {
        super(billAmount, channel, clientIp, status);
        this.agent = agent;
    }

    public selling.sunshine.model.lite.Agent getAgent() {
        return agent;
    }

    public void setAgent(selling.sunshine.model.lite.Agent agent) {
        this.agent = agent;
    }
}
