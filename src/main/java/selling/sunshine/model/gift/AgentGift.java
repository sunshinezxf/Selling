package selling.sunshine.model.gift;

import selling.sunshine.model.Entity;
import selling.sunshine.model.goods.Goods4Agent;
import selling.sunshine.model.lite.Agent;

/**
 * Created by sunshine on 7/1/16.
 */
public class AgentGift extends Entity {
    private String giftId;
    private Agent agent;
    private Goods4Agent goods;
    private int amount;

    public AgentGift() {
        super();
        amount = 0;
    }

    public AgentGift(Agent agent, Goods4Agent goods) {
        this();
        this.agent = agent;
        this.goods = goods;
    }

    public AgentGift(Agent agent, Goods4Agent goods, int amount) {
        this(agent, goods);
        this.amount = amount;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
