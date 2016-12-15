package selling.sunshine.model.bill;

public class BillVolume {
	
	private String date;
	private double amount;
	
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
