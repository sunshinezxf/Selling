package selling.sunshine.vo.order;


import common.sunshine.model.selling.goods.Goods4Agent;

/**
 * 关于某个商品的订单统计信息vo类
 * @author wxd
 */
public class OrderSumOverview {
	
	private Goods4Agent goods;//关联的商品
	private int quantity;//购买总量
	private double totalPrice;//购买总价

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
