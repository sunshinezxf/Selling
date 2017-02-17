package selling.sunshine.model.sum;

/**
 * 代理商的订单统计
 * @author wang_min
 *
 */
public class OrderStatistics {
	
	private String agentName;//代理商姓名
	private int payedQuantity;//已付款的订单数量
	private int unPayedQuantity;//未付款的订单数量
	private int finishedQuantity;//已完成的订单数量
	private int totalQuantity;//总订单数量
	
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public int getPayedQuantity() {
		return payedQuantity;
	}
	public void setPayedQuantity(int payedQuantity) {
		this.payedQuantity = payedQuantity;
	}
	public int getUnPayedQuantity() {
		return unPayedQuantity;
	}
	public void setUnPayedQuantity(int unPayedQuantity) {
		this.unPayedQuantity = unPayedQuantity;
	}
	public int getFinishedQuantity() {
		return finishedQuantity;
	}
	public void setFinishedQuantity(int finishedQuantity) {
		this.finishedQuantity = finishedQuantity;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	

}
