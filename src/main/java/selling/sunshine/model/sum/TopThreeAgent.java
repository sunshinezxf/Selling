package selling.sunshine.model.sum;

/**
 * 排名前三的代理商
 * @author wang_min
 *
 */
public class TopThreeAgent {

	private String agentName;//代理商名称
	private String agentId;//代理商ID
	private int quantity;//销售数量

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

}
