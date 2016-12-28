package selling.sunshine.vo.customer;

import common.sunshine.model.selling.agent.lite.Agent;
import common.sunshine.model.selling.customer.Customer;

import java.sql.Timestamp;

/**
 * Created by sunshine on 2016/12/28.
 */
public class CustomerVo {
    private String customerId;

    private String name;

    private String wechat;

    private String phone;

    private String address;

    private String province;

    private String city;

    private String district;

    private Agent agent;

    private boolean transformed;

    private boolean blockFlag;

    private Timestamp createAt;

    public CustomerVo() {
        super();
    }

    public CustomerVo(Customer customer) {
        this.customerId = customer.getCustomerId();
        this.name = customer.getName();
        this.wechat = customer.getWechat();
        this.phone = customer.getPhone().getPhone();
        this.address = customer.getAddress().getAddress();
        this.province = customer.getAddress().getProvince();
        this.city = customer.getAddress().getCity();
        this.district = customer.getAddress().getDistrict();
        this.agent = customer.getAgent();
        this.transformed = customer.isTransformed();
        this.blockFlag = customer.isBlockFlag();
        this.createAt = customer.getCreateAt();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public boolean isTransformed() {
        return transformed;
    }

    public void setTransformed(boolean transformed) {
        this.transformed = transformed;
    }

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
