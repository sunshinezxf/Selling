package common.sunshine.model.selling.customer;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.agent.lite.Agent;

/**
 * Created by sunshine on 4/8/16.
 */
public class Customer extends Entity {
    private String customerId;
    private String name;
    private CustomerPhone phone;
    private CustomerAddress address;
    private Agent agent;
    private boolean transformed;
    private String wechat;

    public Customer() {
        super();
    }

    public Customer(String name, String address, String phone) {
        super();
        this.name = name;
        if (phone != null) {
            this.phone = new CustomerPhone(phone);
        }
        if (address != null) {
            this.address = new CustomerAddress(address);
        }
    }

    public Customer(String name, String address, String phone, Agent agent) {
        this(name, address, phone);
        this.agent = agent;
    }

    public Customer(String customerId, String name, CustomerPhone phone, CustomerAddress address, Agent agent, boolean transformed) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.agent = agent;
        this.transformed = transformed;
    }

    public Customer(String customerId, String name, CustomerPhone phone, CustomerAddress address, Agent agent, boolean transformed, String wechat) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.agent = agent;
        this.transformed = transformed;
        this.wechat = wechat;
    }

    public void setTransformed(boolean transformed) {
        this.transformed = transformed;
    }

    public boolean isTransformed() {
        return transformed;
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

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public CustomerPhone getPhone() {
        return phone;
    }

    public void setPhone(CustomerPhone phone) {
        this.phone = phone;
    }

    public CustomerAddress getAddress() {
        return address;
    }

    public void setAddress(CustomerAddress address) {
        this.address = address;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    @Override
	public int hashCode() {
		return customerId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(obj instanceof Customer){
			return this.customerId.equals(((Customer)obj).getCustomerId());
		}
		return false;
	}

    
}
