package common.sunshine.model.selling.event.support;

import common.sunshine.model.Entity;


import common.sunshine.model.selling.event.PromotionEvent;
import common.sunshine.model.selling.goods.Goods4Customer;

/**
 * Created by sunshine on 2016/12/16.
 */
public class PromotionConfig extends Entity {
    private String configId;

    private PromotionEvent event;

    private Goods4Customer buyGoods;

    private Goods4Customer giveGoods;

    private int full;

    private int give;

    private int criterion;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public PromotionEvent getEvent() {
        return event;
    }

    public void setEvent(PromotionEvent event) {
        this.event = event;
    }

    public Goods4Customer getBuyGoods() {
        return buyGoods;
    }

    public void setBuyGoods(Goods4Customer buyGoods) {
        this.buyGoods = buyGoods;
    }

    public Goods4Customer getGiveGoods() {
        return giveGoods;
    }

    public void setGiveGoods(Goods4Customer giveGoods) {
        this.giveGoods = giveGoods;
    }

    public int getFull() {
        return full;
    }

    public void setFull(int full) {
        this.full = full;
    }

    public int getGive() {
        return give;
    }

    public void setGive(int give) {
        this.give = give;
    }

    public int getCriterion() {
        return criterion;
    }

    public void setCriterion(int criterion) {
        this.criterion = criterion;
    }
}
