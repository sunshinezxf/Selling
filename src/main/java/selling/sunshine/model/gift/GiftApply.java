package selling.sunshine.model.gift;

import selling.sunshine.model.lite.Agent;
import selling.sunshine.model.Entity;
import selling.sunshine.model.goods.Goods4Agent;

/**
 * Created by sunshine on 8/5/16.
 */
public class GiftApply extends Entity {
    private String applyId;
    private int potential;
    private int line;
    private Agent agent;
    private Goods4Agent goods;
    private GiftApplyStatus status;

    private GiftApply() {
        super();
    }

    public GiftApply(int potential, int line, Agent agent, Goods4Agent goods) {
        this();
        this.potential = potential;
        this.line = line;
        this.agent = agent;
        this.goods = goods;
        this.status = GiftApplyStatus.APPLYED;
    }

    public GiftApply(int potential, int line, Agent agent, Goods4Agent goods, GiftApplyStatus status) {
        this(potential, line, agent, goods);
        this.status = status;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public int getPotential() {
        return potential;
    }

    public void setPotential(int potential) {
        this.potential = potential;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Goods4Agent getGoods() {
        return goods;
    }

    public void setGoods(Goods4Agent goods) {
        this.goods = goods;
    }

    public GiftApplyStatus getStatus() {
        return status;
    }

    public void setStatus(GiftApplyStatus status) {
        this.status = status;
    }
}
