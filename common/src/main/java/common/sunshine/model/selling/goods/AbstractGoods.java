package common.sunshine.model.selling.goods;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.goods.support.GoodsType;

/**
 * 抽象商品类, 所有商品类需继承此类
 * Created by sunshine on 6/16/16.
 */
public abstract class AbstractGoods extends Entity {
    private String goodsId;

    /* 商品名称 */
    private String name;

    /* 商品别名, 后台统计图表中使用 */
    private String nickname;

    /* 商品描述 */
    private String description;

    /* 商品规格 */
    private String standard;

    /* 商品的计量单位 */
    private String measure;

    /* 生产批号 */
    private String produceNo;

    /* 生产日期 */
    private String produceDate;

    /* 商品类型*/
    private GoodsType type;

    /* 商品列表中的位置, 用于固定顾客看到的商品列表中各商品的出现位置 */
    private int position;

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
