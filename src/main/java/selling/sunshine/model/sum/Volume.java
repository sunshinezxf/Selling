package selling.sunshine.model.sum;

/**
 * Created by sunshine on 8/5/16.
 */
public class Volume {
    private String agentId;
    private String goodsId;
    private int quantity;
    private double price;

    public Volume() {
        super();
    }

    public Volume(String agentId, String goodsId, int quantity) {
        this();
        this.agentId = agentId;
        this.goodsId = goodsId;
        this.quantity = quantity;
    }

    public Volume(String agentId, String goodsId, int quantity, double price) {
        this(agentId, goodsId, quantity);
        this.price = price;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}
