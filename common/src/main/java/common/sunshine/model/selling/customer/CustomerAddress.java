package common.sunshine.model.selling.customer;

import common.sunshine.model.Entity;

/**
 * Created by sunshine on 5/5/16.
 */
public class CustomerAddress extends Entity {
    private String addressId;
    private String address;
    private Customer customer;
    private String province;
    private String city;
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
    public boolean equals(Object obj){
    	if(obj instanceof CustomerAddress){
    		return this.address.equals(((CustomerAddress)obj).getAddress());
    	}
    	return false;
    }
    
    @Override
    public int hashCode(){
    	return address.hashCode();
    }
}
