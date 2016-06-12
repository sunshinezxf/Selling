package selling.sunshine.model;

/**
 * Created by sunshine on 5/5/16.
 */
public class CustomerAddress extends Entity {
    private String addressId;
    private String address;
    private Customer customer;

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
