package selling.sunshine.vo.order;

import common.sunshine.model.selling.agent.lite.Agent;
import common.sunshine.model.selling.goods.Goods4Agent;

public class OrderSumOverview {
	
	private Goods4Agent goods;
	
	private int quantity;
	
	private double totalPrice;

	
	public Goods4Agent getGoods() {
		return goods;
	}

	public void setGoods(Goods4Agent goods) {
		this.goods = goods;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
}
