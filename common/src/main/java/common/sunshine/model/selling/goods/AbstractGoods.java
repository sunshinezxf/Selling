package common.sunshine.model.selling.goods;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.goods.support.GoodsType;

/**
 * Created by sunshine on 6/16/16.
 */
public abstract class AbstractGoods extends Entity {
    private String goodsId;
    private String name;
    private String nickname;
    private String description;
    private String standard;
    //商品的计量单位
    private String measure;

    private String produceNo;

    private String produceDate;

    private GoodsType type;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getProduceNo() {
        return produceNo;
    }

    public void setProduceNo(String produceNo) {
        this.produceNo = produceNo;
    }

    public String getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(String produceDate) {
        this.produceDate = produceDate;
    }

    public GoodsType getType() {
        return type;
    }

    public void setType(GoodsType type) {
        this.type = type;
    }
}
