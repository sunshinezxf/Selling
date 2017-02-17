package common.sunshine.model.selling.customer;

import common.sunshine.model.Entity;

/**
 * 该类为顾客电话
 * Created by sunshine on 5/5/16.
 */
public class CustomerPhone extends Entity {
    private String phoneId;

    /* 顾客手机号 */
    private String phone;

    /* 顾客信息 */
    private Customer customer;

    public CustomerPhone() {
        super();
    }

    public CustomerPhone(String phone) {
        this();
        this.phone = phone;
    }

    public CustomerPhone(String phone, Customer customer) {
        this(phone);
        this.customer = customer;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
