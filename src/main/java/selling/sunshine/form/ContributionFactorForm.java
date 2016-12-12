package selling.sunshine.form;

import javax.validation.constraints.NotNull;

public class ContributionFactorForm {

	@NotNull
	private String quantityWeight;

	@NotNull
	private String priceWeight;

	@NotNull
	private String directAgentQuantityWeight;

	public String getQuantityWeight() {
		return quantityWeight;
	}

	public void setQuantityWeight(String quantityWeight) {
		this.quantityWeight = quantityWeight;
	}

	public String getPriceWeight() {
		return priceWeight;
	}

	public void setPriceWeight(String priceWeight) {
		this.priceWeight = priceWeight;
	}

	public String getDirectAgentQuantityWeight() {
		return directAgentQuantityWeight;
	}

	public void setDirectAgentQuantityWeight(String directAgentQuantityWeight) {
		this.directAgentQuantityWeight = directAgentQuantityWeight;
	}

}
