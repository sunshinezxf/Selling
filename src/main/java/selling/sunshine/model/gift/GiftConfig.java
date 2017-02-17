package selling.sunshine.model.gift;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.agent.lite.Agent;
import common.sunshine.model.selling.goods.Goods4Agent;

/**
 * Created by sunshine on 7/1/16.
 */
public class GiftConfig extends Entity {
    private String giftId;//赠送ID
    private Agent agent;//关联代理商
    private Goods4Agent goods;//关联商品
    private int amount;//赠送的数量

    public GiftConfig() {
        super();
        amount = 0;
    }

    public GiftConfig(Agent agent, Goods4Agent goods) {
        this();
        this.agent = agent;
        this.goods = goods;
    }

    public GiftConfig(Agent agent, Goods4Agent goods, int amount) {
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
