package selling.sunshine.form;

import javax.validation.constraints.NotNull;

public class AgentVitalityForm {
	
	 @NotNull
	 private String vitalityQuantity;
	 
	 @NotNull
	 private String vitalityPrice;

	public String getVitalityQuantity() {
		return vitalityQuantity;
	}

	public void setVitalityQuantity(String vitalityQuantity) {
		this.vitalityQuantity = vitalityQuantity;
	}

	public String getVitalityPrice() {
		return vitalityPrice;
	}

	public void setVitalityPrice(String vitalityPrice) {
		this.vitalityPrice = vitalityPrice;
	}
	 
	 

}
