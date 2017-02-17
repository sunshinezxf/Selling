package selling.sunshine.model.sum;
/**
 * 月销量统计
 * @author wang_min
 *
 */
public class OrderMonth {

	private int quantity;//当月订单数量
	private double price;//当月订单金额
	private int totalQuantity;//订单总数量
	private double totalPrice;//订单总金额
	private int num;//当月代理商人数
	private int totalNum;//总代理商认识
	private int payedMonth;//当月已付款状态订单
	private int shippedMonth;//当月已发货状态订单
	private int giftMonth;//当月赠送的订单

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
