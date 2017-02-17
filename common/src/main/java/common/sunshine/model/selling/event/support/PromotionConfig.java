package common.sunshine.model.selling.event.support;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.event.PromotionEvent;
import common.sunshine.model.selling.goods.Goods4Customer;

/**
 * 该类为买赠活动配置
 * Created by sunshine on 2016/12/16.
 */
public class PromotionConfig extends Entity {
    private String configId;

    /* 买赠活动 */
    private PromotionEvent event;

    /* 购买商品 */
    private Goods4Customer buyGoods;

    /* 赠送商品 */
    private Goods4Customer giveGoods;

    /* 每买数量full */
    private int full;

    /* 赠送数量give */
    private int give;

    /* 满足基准数量 */
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
