package selling.sunshine.model.sum;

/**
 * Created by sunshine on 6/24/16.
 * 订单统计，可能已被废弃
 */
public class Sum4Order {
    private String goodsId;
    private String goodsName;
    private int quantity;
    private int num;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
