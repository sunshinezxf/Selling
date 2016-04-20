package selling.sunshine.model;

import org.springframework.util.StringUtils;

/**
 * Created by sunshine on 4/7/16.
 */
public class Agent extends Entity {
    private String agentId;
    private String name;
    private String gender;
    private String phone;
    private String address;
    private String wechat;
    private boolean paid;
    private boolean granted;

    public Agent() {
        super();
        paid = false;
        granted = false;
    }

    public Agent(String name, String gender, String phone, String address) {
        this();
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }

    public Agent(String name, String gender, String phone, String address, String wechat) {
        this(name, gender, phone, address);
        if (!StringUtils.isEmpty(wechat)) {
            this.wechat = wechat;
        }
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }
}
