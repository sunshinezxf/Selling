package selling.sunshine.model.sum;

import common.sunshine.model.selling.goods.Goods4Agent;

public class VolumeTotal {

	private String agentId;
	private Goods4Agent goods;
	private int totalQuantity;
	private double totalPrice;
	private int yearQuantity;
	private double yearPrice;
	private int monthQuantity;
	private double monthPrice;

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
