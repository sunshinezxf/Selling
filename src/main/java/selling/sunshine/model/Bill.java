package selling.sunshine.model;

/**
 * Created by sunshine on 5/10/16.
 */
public abstract class Bill extends Entity {
    private String billId;
    private String clientIp;
    private String channel;
    private double billAmount;
    private BillStatus status;

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
