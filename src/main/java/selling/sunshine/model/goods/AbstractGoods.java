package selling.sunshine.model.goods;

import selling.sunshine.model.Entity;

/**
 * Created by sunshine on 6/16/16.
 */
public abstract class AbstractGoods extends Entity {
    private String goodsId;
    private String name;
    private String description;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
