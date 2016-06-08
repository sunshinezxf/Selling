package selling.sunshine.form;

import javax.validation.constraints.NotNull;

public class BankCardForm {
	@NotNull
	private String bankCardNo;
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	
	
}
