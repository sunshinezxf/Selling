package selling.sunshine.model;

/**
 * Created by sunshine on 4/8/16.
 */
public class Customer extends Entity {
	private String customerId;
	private String name;
	private String address;
	private String phone;

	public Customer() {
		super();
	}

	public Customer(String customerId, String name, String address, String phone) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.address = address;
		this.phone = phone;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
