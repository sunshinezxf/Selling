package selling.sunshine.model.sum;

/**
 * 该类统计各个商品的统计信息
 */
public class Vendition {

    /* 商品的id */
    private String goodsId;

    /* 商品的名称 */
    private String goodsName;

    /* 订单的类型 */
    private int orderType;

    /* 商品的数量 */
    private int goodsQuantity;

    /* 商品的价格 */
    private double recordPrice;

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

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getGoodsQuantity() {
        return goodsQuantity;
    }

    public void setGoodsQuantity(int goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }

    public double getRecordPrice() {
        return recordPrice;
    }

    public void setRecordPrice(double recordPrice) {
        this.recordPrice = recordPrice;
    }
}
