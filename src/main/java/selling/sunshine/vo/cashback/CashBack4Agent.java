package selling.sunshine.vo.cashback;

import common.sunshine.model.selling.agent.lite.Agent;

/**
 * 代理商总的返现信息
 * Created by sunshine on 8/10/16.
 */
public class CashBack4Agent {
    private Agent agent;//关联的代理商

    private double amount; //代理商自身购买的数量

    private int sPieces; //自身购买商品数量

    private double self; //自身返现金额

    private int dPieces;//上级代理商购买商品数量

    private double direct;//上级代理商返现金额

    private int iPieces;//上上级代理商购买商品数量

    private double indirect;//上上级代理商返现金额

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

    public int getsPieces() {
        return sPieces;
    }

    public void setsPieces(int sPieces) {
        this.sPieces = sPieces;
    }

    public int getdPieces() {
        return dPieces;
    }

    public void setdPieces(int dPieces) {
        this.dPieces = dPieces;
    }

    public int getiPieces() {
        return iPieces;
    }

    public void setiPieces(int iPieces) {
        this.iPieces = iPieces;
    }
}
