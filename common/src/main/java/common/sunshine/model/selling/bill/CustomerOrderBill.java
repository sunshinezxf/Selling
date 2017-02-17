package common.sunshine.model.selling.bill;

import common.sunshine.model.selling.bill.support.BillStatus;
import common.sunshine.model.selling.order.CustomerOrder;

/**
 * 该类为顾客订单所对应的账单, 继承抽象账单类
 * Created by sunshine on 23/08/16.
 */
public class CustomerOrderBill extends Bill {

    /* 账单所对应的顾客订单 */
    private CustomerOrder customerOrder;

    public CustomerOrderBill() {
        super();
    }

    public CustomerOrderBill(double billAmount, String channel, String clientIp, CustomerOrder customerOrder) {
        super(billAmount, channel, clientIp);
        this.customerOrder = customerOrder;
    }

    public CustomerOrderBill(double billAmount, String channel, String clientIp, BillStatus status, CustomerOrder customerOrder) {
        super(billAmount, channel, clientIp, status);
        this.customerOrder = customerOrder;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }
}
