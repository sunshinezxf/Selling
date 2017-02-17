package common.sunshine.model.selling.bill;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.bill.support.BillStatus;

/**
 * 账单抽象类, 所有的账单类继承此方法
 * Created by sunshine on 5/10/16.
 */
public abstract class Bill extends Entity {
    private String billId;

    /* 客户端ip */
    private String clientIp;

    /* 账单付款渠道 */
    private String channel;

    /* 账单金额 */
    private double billAmount;

    /* 账单状态 */
    private BillStatus status;

    /**
     * 默认构造方法, 账单默认未付款
     */
    public Bill() {
        super();
        status = BillStatus.NOT_PAYED;
    }

    public Bill(double billAmount, String channel) {
        this();
        this.billAmount = billAmount;
        this.channel = channel;
    }

    public Bill(double billAmount, String channel, String clientIp) {
        this(billAmount, channel);
        this.clientIp = clientIp;
    }

    public Bill(double billAmount, String channel, String clientIp, BillStatus status) {
        this(billAmount, channel, clientIp);
        this.status = status;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }
}
