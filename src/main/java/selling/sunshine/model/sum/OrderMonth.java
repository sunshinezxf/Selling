package selling.sunshine.model.sum;

public class OrderMonth {

	private int quantity;
	private double price;
	private int totalQuantity;
	private double totalPrice;
	private int num;
	private int totalNum;
	private int payedMonth;
	private int shippedMonth;
	private int giftMonth;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getPayedMonth() {
		return payedMonth;
	}

	public void setPayedMonth(int payedMonth) {
		this.payedMonth = payedMonth;
	}

	public int getShippedMonth() {
		return shippedMonth;
	}

	public void setShippedMonth(int shippedMonth) {
		this.shippedMonth = shippedMonth;
	}

	public int getGiftMonth() {
		return giftMonth;
	}

	public void setGiftMonth(int giftMonth) {
		this.giftMonth = giftMonth;
	}

}
