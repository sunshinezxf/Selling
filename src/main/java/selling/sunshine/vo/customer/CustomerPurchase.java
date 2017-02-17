package selling.sunshine.vo.customer;

import common.sunshine.model.selling.agent.lite.Agent;

/**
 * 顾客购买信息vo类
 * Created by sunshine on 2016/12/28.
 */
public class CustomerPurchase {
    private String customerId;//顾客ID

    private String customerName;//顾客姓名

    private Agent agent;//顾客所属代理商

    private double montant;//购买金额

    private int dealNum;//购买数量

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Agent getAgent() {
        return agent;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public int getDealNum() {
        return dealNum;
    }

    public void setDealNum(int dealNum) {
        this.dealNum = dealNum;
    }
}
