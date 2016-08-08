package selling.sunshine.model.sum;

/**
 * Created by sunshine on 8/5/16.
 */
public class Volume {
    private String agentId;
    private String goodsId;
    private int quantity;

    public Volume() {

    }

    public Volume(String agentId, String goodsId, int quantity) {
        this.agentId = agentId;
        this.goodsId = goodsId;
        this.quantity = quantity;
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
}
