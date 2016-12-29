package selling.sunshine.vo.customer;

import common.sunshine.model.selling.agent.lite.Agent;

/**
 * Created by sunshine on 2016/12/28.
 */
public class CustomerPurchase {
    private String customerId;

    private String customerName;

    private Agent agent;

    private double montant;

    private int dealNum;

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
