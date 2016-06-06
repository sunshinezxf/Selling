package selling.sunshine.model;

import selling.sunshine.model.lite.Agent;

/**
 * Created by sunshine on 4/8/16.
 */
public class Customer extends Entity {
    private String customerId;
    private String name;
    private CustomerPhone phone;
    private CustomerAddress address;
    private Agent agent;

    public Customer() {
        super();
    }

    public Customer(String name, String address, String phone) {
        super();
        this.name = name;
        if(phone != null){
        	this.phone = new CustomerPhone(phone);
        }
        if(address != null){
        	this.address = new CustomerAddress(address);
        }
    }

    public Customer(String name, String address, String phone, Agent agent) {
        this(name, address, phone);
        this.agent = agent;
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


}
