package common.sunshine.model.selling.customer;

import common.sunshine.model.Entity;

/**
 * 该类为顾客地址
 * Created by sunshine on 5/5/16.
 */
public class CustomerAddress extends Entity {
    private String addressId;

    /* 地址信息 */
    private String address;

    /* 所属顾客 */
    private Customer customer;

    /* 省 */
    private String province;

    /* 市 */
    private String city;

    /* 区 */
    private String district;

    public CustomerAddress() {
        super();
    }

    public CustomerAddress(String address) {
        this();
        this.address = address;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomerAddress) {
            return this.address.equals(((CustomerAddress) obj).getAddress());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}
