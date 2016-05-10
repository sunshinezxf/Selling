package selling.sunshine.model;

/**
 * Created by sunshine on 5/10/16.
 */
public class DepositBill extends Bill {
    private Agent agent;

    public DepositBill() {
        super();
        agent = new Agent();
    }

    public DepositBill(double billAmount, String clientIp, Agent agent) {
        super(billAmount, clientIp);
        this.agent = agent;
    }

    public DepositBill(double billAmount, String clientIp, BillStatus status, Agent agent) {
        super(billAmount, clientIp, status);
        this.agent = agent;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
