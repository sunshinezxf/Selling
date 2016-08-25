package promotion.sunshine.form;

import javax.validation.constraints.NotNull;

public class EventApplicationForm {
	@NotNull
	private String donor_name;
	@NotNull
	private String donor_phone;
	@NotNull
	private String donee_name;
	@NotNull
	private String donee_gender;
	@NotNull
	private String donee_phone;
	@NotNull
	private String donee_address;
	@NotNull
	private String relation;
	
	private String wishes;
	
	private String optionId[];

	public String getDonor_name() {
		return donor_name;
	}

	public void setDonor_name(String donor_name) {
		this.donor_name = donor_name;
	}

	public String getDonor_phone() {
		return donor_phone;
	}

	public void setDonor_phone(String donor_phone) {
		this.donor_phone = donor_phone;
	}

	public String getDonee_name() {
		return donee_name;
	}

	public void setDonee_name(String donee_name) {
		this.donee_name = donee_name;
	}

	public String getDonee_gender() {
		return donee_gender;
	}

	public void setDonee_gender(String donee_gender) {
		this.donee_gender = donee_gender;
	}

	public String getDonee_phone() {
		return donee_phone;
	}

	public void setDonee_phone(String donee_phone) {
		this.donee_phone = donee_phone;
	}

	public String getDonee_address() {
		return donee_address;
	}

	public void setDonee_address(String donee_address) {
		this.donee_address = donee_address;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getWishes() {
		return wishes;
	}

	public void setWishes(String wishes) {
		this.wishes = wishes;
	}

	public String[] getOptionId() {
		return optionId;
	}

	public void setOptionId(String[] optionId) {
		this.optionId = optionId;
	}

}
