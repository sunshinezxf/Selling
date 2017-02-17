package selling.sunshine.model;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.agent.lite.Agent;

/**
 * Created by sunshine on 6/18/16.
 */
public class WithdrawRecord extends Entity {
    private String withdrawId;//提现ID
    private Agent agent;//关联代理商
    private String openId;//微信openID
    private double wealth;//提现前余额
    private double amount;//提现数量
    private WithdrawStatus status;//提现状态

    public WithdrawRecord() {
        super();
        this.setBlockFlag(true);
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
