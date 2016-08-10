package selling.sunshine.form;

public class OrderItemForm {
    private String[] orderItemId;
    private String[] customerId;
    private String[] goodsId;
    private String[] goodsQuantity;
    private String[] orderItemPrice;
    private String[] address;
    private String[] description;
    private String agentId;
    private String orderId;

    public String[] getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String[] orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String[] getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String[] customerId) {
        this.customerId = customerId;
    }

    public String[] getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String[] goodsId) {
        this.goodsId = goodsId;
    }

    public String[] getGoodsQuantity() {
        return goodsQuantity;
    }

    public void setGoodsQuantity(String[] goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }

    public String[] getOrderItemPrice() {
        return orderItemPrice;
    }

    public void setOrderItemPrice(String[] orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String[] getAddress() {
        return address;
    }

    public void setAddress(String[] address) {
        this.address = address;
    }

}
