package common.sunshine.model.selling.event;

import common.sunshine.model.selling.event.support.PromotionConfig;

import java.util.List;

/**
 * 该类为买赠活动
 * Created by sunshine on 2016/12/16.
 */
public class PromotionEvent extends Event {
    /* 活动的买赠商品配置 */
    private List<PromotionConfig> config;

    public List<PromotionConfig> getConfig() {
        return config;
    }

    public void setConfig(List<PromotionConfig> config) {
        this.config = config;
    }
}
