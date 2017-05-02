package common.sunshine.model.selling.vouchers;

import common.sunshine.model.Entity;

/**
 * Created by wxd on 2017/5/2.
 */
public class Vouchers extends Entity {

    private String vouchersId;

    /* 代理商 */
    private String agentId;

    /* 金额 */
    private double price;

    /* 是否已使用，0代表还没使用，1代表已经使用 */
    private boolean used;

    public Vouchers() {
        super();
    }

    public Vouchers(String vouchersId, String agentId, double price, boolean used) {
        super();
        this.vouchersId = vouchersId;
        this.agentId = agentId;
        this.price = price;
        this.used = used;
    }

    public String getVouchersId() {
        return vouchersId;
    }

    public String getAgentId() {
        return agentId;
    }

    public double getPrice() {
        return price;
    }

    public boolean isUsed() {
        return used;
    }

    public void setVouchersId(String vouchersId) {
        this.vouchersId = vouchersId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
