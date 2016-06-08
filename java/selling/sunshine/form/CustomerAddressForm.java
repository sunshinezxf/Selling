package selling.sunshine.form;

import javax.validation.constraints.NotNull;

public class CustomerAddressForm {
	@NotNull
	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
