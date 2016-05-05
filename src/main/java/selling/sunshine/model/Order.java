package selling.sunshine.model;

/**
 * Created by sunshine on 4/8/16.
 */
public class Order {
	private String orderId;
	private String agentId;
	private String createTime;

	public Order(String orderId, String agentId, String createTime) {
		this.orderId = orderId;
		this.agentId = agentId;
		this.createTime = createTime;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
