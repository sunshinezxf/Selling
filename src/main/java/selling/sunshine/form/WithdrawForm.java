package selling.sunshine.form;

import javax.validation.constraints.NotNull;

public class WithdrawForm {
	@NotNull
	private String bankCardNo;
	@NotNull
	private double money;
	
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	
	
	
}
