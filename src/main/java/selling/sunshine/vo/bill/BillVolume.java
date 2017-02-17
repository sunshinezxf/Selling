package selling.sunshine.vo.bill;

/**
 * 按照日期统计的账单金额
 */
public class BillVolume {
	
	private String date; //日期
	private double amount;//账单金额
	
	public BillVolume() {
		super();
	}	

	public BillVolume(String date, double amount) {
		super();
		this.date = date;
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	

}
