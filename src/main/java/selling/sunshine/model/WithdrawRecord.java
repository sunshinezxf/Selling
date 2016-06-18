package selling.sunshine.model;

/**
 * Created by sunshine on 6/18/16.
 */
public class WithdrawRecord {
    private String withdrawId;
    private Agent agent;
    private String openId;
    private double wealth;
    private double amount;
    private WithdrawStatus status;

    public WithdrawRecord() {
        super();
    }

    public WithdrawRecord(String openId, double wealth, double amount) {
        this();
        this.openId = openId;
        this.wealth = wealth;
        this.amount = amount;
    }

    public WithdrawRecord(String openId, double wealth, double amount, WithdrawStatus status) {
        this(openId, wealth, amount);
        this.status = status;
    }

    public String getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(String transferId) {
        this.withdrawId = transferId;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public double getWealth() {
        return wealth;
    }

    public void setWealth(double wealth) {
        this.wealth = wealth;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public WithdrawStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawStatus status) {
        this.status = status;
    }
}
