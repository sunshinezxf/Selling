package selling.sunshine.model;

import selling.sunshine.model.lite.Agent;

/**
 * Created by sunshine on 6/6/16.
 */
public class WithdrawRecord extends Entity {
    private String withdrawId;
    private Agent agent;
    private String bankCardNo;
    private double wealth;
    private double amount;
    private WithdrawStatus status;

    public WithdrawRecord() {
        super();
        this.status = WithdrawStatus.APPLY;
    }

    public WithdrawRecord(double amount) {
        this();
        this.amount = amount;
    }

    public WithdrawRecord(double amount, WithdrawStatus status) {
        this(amount);
        this.status = status;
    }

    public WithdrawRecord(double amount, WithdrawStatus status, Agent agent) {
        this(amount, status);
        this.agent = agent;
    }

    public String getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(String withdrawId) {
        this.withdrawId = withdrawId;
    }

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

    public WithdrawStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawStatus status) {
        this.status = status;
    }

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public double getWealth() {
		return wealth;
	}

	public void setWealth(double wealth) {
		this.wealth = wealth;
	}
}
