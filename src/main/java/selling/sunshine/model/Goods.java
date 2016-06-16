package selling.sunshine.model;

import java.util.List;

/**
 * Created by sunshine on 4/8/16.
 */
public class Goods extends Entity {
    private String goodsId;
    private String name;
    private double price;
    private double benefit;
    private String description;
    private List<GoodsThumbnail> thumbnails;

    public Goods() {
        super();
    }

    public Goods(String name, double price, double benefit) {
        this();
        this.name = name;
        this.price = price;
        this.benefit = benefit;
    }

    public Goods(String name, double price, double benefit, String description) {
        this(name, price, benefit);
        this.description = description;
    }

    public Goods(String name, double price, double benefit, String description, boolean block) {
        this(name, price, benefit, description);
        this.setBlockFlag(block);
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBenefit() {
        return benefit;
    }

    public void setBenefit(double benefit) {
        this.benefit = benefit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GoodsThumbnail> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<GoodsThumbnail> thumbnails) {
        this.thumbnails = thumbnails;
    }
}
