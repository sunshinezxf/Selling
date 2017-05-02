package common.sunshine.model.selling.vouchers;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.agent.lite.Agent;
import common.sunshine.model.selling.vouchers.support.VouchersType;

/**
 * Created by wxd on 2017/5/2.
 */
public class Vouchers extends Entity {

    private String vouchersId;

    /* 代理商 */
    private Agent agent;

    /* 金额 */
    private double price;

    /* 是否已使用，0代表还没使用，1代表已经使用 */
    private boolean used;

    private VouchersType type;

    public Vouchers() {
        super();
    }

    public Vouchers(String vouchersId, Agent agent, double price, boolean used, VouchersType type) {
        this.vouchersId = vouchersId;
        this.agent = agent;
        this.price = price;
        this.used = used;
        this.type = type;
    }

    public String getVouchersId() {
        return vouchersId;
    }

    public Agent getAgent() {
        return agent;
    }

    public double getPrice() {
        return price;
    }

    public boolean isUsed() {
        return used;
    }

    public VouchersType getType() {
        return type;
    }

    public void setVouchersId(String vouchersId) {
        this.vouchersId = vouchersId;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setType(VouchersType type) {
        this.type = type;
    }
}
