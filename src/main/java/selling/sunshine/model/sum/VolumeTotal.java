package selling.sunshine.model.sum;

import common.sunshine.model.selling.goods.Goods4Agent;

public class VolumeTotal {

	private String agentId;//代理商ID
	private Goods4Agent goods;//代理商商品
	private int totalQuantity;//总卖的数量
	private double totalPrice;//总卖的金额
	private int yearQuantity;//年卖的数量
	private double yearPrice;//年卖的金额
	private int monthQuantity;//月卖的数量
	private double monthPrice;//月卖的金额

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
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

	public int getYearQuantity() {
		return yearQuantity;
	}

	public void setYearQuantity(int yearQuantity) {
		this.yearQuantity = yearQuantity;
	}

	public double getYearPrice() {
		return yearPrice;
	}

	public void setYearPrice(double yearPrice) {
		this.yearPrice = yearPrice;
	}

	public int getMonthQuantity() {
		return monthQuantity;
	}

	public void setMonthQuantity(int monthQuantity) {
		this.monthQuantity = monthQuantity;
	}

	public double getMonthPrice() {
		return monthPrice;
	}

	public void setMonthPrice(double monthPrice) {
		this.monthPrice = monthPrice;
	}

	public Goods4Agent getGoods() {
		return goods;
	}

	public void setGoods(Goods4Agent goods) {
		this.goods = goods;
	}
	
	

}
