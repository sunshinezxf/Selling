package selling.sunshine.vo.cashback;

import common.sunshine.model.selling.agent.lite.Agent;

/**
 * 某一个月的返现信息vo类
 * @author sunshine
 */
public class CashBack {
	private Agent agent; //关联的代理商
	
	private String month;  //月份
	
	private int quantity; //购买数量
	
	private double amount; //返现金额
	
	private int level;  //级别（自身，上级代理商，上上级代理商）

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
