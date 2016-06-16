package selling.sunshine.model;

import selling.sunshine.model.goods.Goods4Agent;
import selling.sunshine.model.goods.Goods4Customer;

import java.sql.Date;

public class OrderPool extends Entity {

	private String orderPoolId;
	private int quantity;
	private double price;
	private double refundAmount;
	private Date poolDate;
	private Agent agent;
	private Goods4Agent goods;
	private RefundConfig refundConfig;

	public OrderPool() {
		super();
		this.setBlockFlag(true);
	}

	public OrderPool(String orderPoolId, int quantity, double price, double refundAmount, Date poolDate, Agent agent, Goods4Customer goods, RefundConfig refundConfig) {
		this();
		this.orderPoolId = orderPoolId;
		this.quantity = quantity;
		this.price = price;
		this.refundAmount = refundAmount;
		this.poolDate = poolDate;
		this.agent = agent;
		this.goods = goods;
		this.refundConfig = refundConfig;
	}

	public String getOrderPoolId() {
		return orderPoolId;
	}

	public void setOrderPoolId(String orderPoolId) {
		this.orderPoolId = orderPoolId;
	}

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

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Date getPoolDate() {
		return poolDate;
	}

	public void setPoolDate(Date poolDate) {
		this.poolDate = poolDate;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public Goods4Agent getGoods() {
		return goods;
	}

	public void setGoods(Goods4Agent goods) {
		this.goods = goods;
	}

	public RefundConfig getRefundConfig() {
		return refundConfig;
	}

	public void setRefundConfig(RefundConfig refundConfig) {
		this.refundConfig = refundConfig;
	}

	
	

}
