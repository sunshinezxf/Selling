package common.sunshine.model.selling.event;

import common.sunshine.model.selling.event.support.PromotionConfig;

import java.util.List;

/**
 * Created by sunshine on 2016/12/16.
 */
public class PromotionEvent extends Event {
    private List<PromotionConfig> config;

    public List<PromotionConfig> getConfig() {
        return config;
    }

    public void setConfig(List<PromotionConfig> config) {
        this.config = config;
    }
}
