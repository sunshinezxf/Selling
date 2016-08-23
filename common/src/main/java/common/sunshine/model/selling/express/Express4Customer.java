package common.sunshine.model.selling.express;

import common.sunshine.model.selling.order.CustomerOrder;

import java.io.Serializable;

/**
 * Created by sunshine on 6/22/16.
 */
public class Express4Customer extends Express implements Serializable {
    private CustomerOrder order;

    public Express4Customer() {
        super();
    }

    public Express4Customer(String expressNumber) {
        super(expressNumber);
    }

    public Express4Customer(String senderName, String senderPhone, String senderAddress, String receiverName, String receiverPhone, String receiverAddress) {
        super(senderName, senderPhone, senderAddress, receiverName, receiverPhone, receiverAddress);
    }

    public Express4Customer(String senderName, String senderPhone, String senderAddress, String receiverName, String receiverPhone, String receiverAddress, String goodsName) {
        super(senderName, senderPhone, senderAddress, receiverName, receiverPhone, receiverAddress, goodsName);
    }

    public Express4Customer(String expressNumber, String senderName, String senderPhone, String senderAddress, String receiverName, String receiverPhone, String receiverAddress, String goodsName) {
        super(expressNumber, senderName, senderPhone, senderAddress, receiverName, receiverPhone, receiverAddress, goodsName);
    }

    public CustomerOrder getOrder() {
        return order;
    }

    public void setOrder(CustomerOrder order) {
        this.order = order;
        this.setLinkId(order.getOrderId());
    }
}
