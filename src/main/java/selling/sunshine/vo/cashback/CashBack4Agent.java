package selling.sunshine.vo.cashback;

import common.sunshine.model.selling.agent.lite.Agent;

/**
 * Created by sunshine on 8/10/16.
 */
public class CashBack4Agent {
    private Agent agent;

    private double amount;

    private int sPieces;

    private double self;

    private int dPieces;

    private double direct;

    private int iPieces;

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
