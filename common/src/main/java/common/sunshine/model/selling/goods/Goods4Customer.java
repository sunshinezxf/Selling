package common.sunshine.model.selling.goods;

import java.util.List;

/**
 * Created by sunshine on 6/16/16.
 */
public class Goods4Customer extends Goods4Agent {
    private double customerPrice;

    private List<Thumbnail> thumbnails;

    public Goods4Customer() {
        super();
    }

    public Goods4Customer(String name, String description, String standard) {
        super(name, description, standard);
    }
    
    public Goods4Customer(String name, double agentPrice, double customerPrice, String description, String standard) {
        super(name, agentPrice, description, standard);
        this.customerPrice = customerPrice;
    }

    public Goods4Customer(String name, double agentPrice, double customerPrice, String description, String standard, List<Thumbnail> thumbnails) {
        this(name, agentPrice, customerPrice, description, standard);
        if (!thumbnails.isEmpty()) {
            this.thumbnails = thumbnails;
        }
    }

    public Goods4Customer(String name, double agentPrice, double customerPrice, String description, String standard, List<Thumbnail> thumbnails, boolean blockFlag) {
        this(name, agentPrice, customerPrice, description, standard, thumbnails);
        this.setBlockFlag(blockFlag);
    }

    public double getCustomerPrice() {
        return customerPrice;
    }

    public void setCustomerPrice(double customerPrice) {
        this.customerPrice = customerPrice;
    }

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<Thumbnail> thumbnails) {
        this.thumbnails = thumbnails;
    }
}
