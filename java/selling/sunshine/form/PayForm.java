package selling.sunshine.form;

import javax.validation.constraints.NotNull;

public class PayForm {
	@NotNull
	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
}
