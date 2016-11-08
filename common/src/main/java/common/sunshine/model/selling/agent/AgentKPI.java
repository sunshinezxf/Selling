package common.sunshine.model.selling.agent;

import common.sunshine.model.Entity;

public class AgentKPI extends Entity {
	private String kpiId;
	private String agentId;
	private String agentName;
	private int customerQuantity;
	private int directAgentQuantity;
	private int agentContribution;

	public AgentKPI(String kpiId, String agentId, String agentName, int customerQuantity, int directAgentQuantity,
			int agentContribution) {
		super();
		this.kpiId = kpiId;
		this.agentId = agentId;
		this.agentName = agentName;
		this.customerQuantity = customerQuantity;
		this.directAgentQuantity = directAgentQuantity;
		this.agentContribution = agentContribution;
	}
	
	

	public AgentKPI() {
		super();
	}


	public String getKpiId() {
		return kpiId;
	}

	public void setKpiId(String kpiId) {
		this.kpiId = kpiId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public int getCustomerQuantity() {
		return customerQuantity;
	}

	public void setCustomerQuantity(int customerQuantity) {
		this.customerQuantity = customerQuantity;
	}

	public int getDirectAgentQuantity() {
		return directAgentQuantity;
	}

	public void setDirectAgentQuantity(int directAgentQuantity) {
		this.directAgentQuantity = directAgentQuantity;
	}

	public int getAgentContribution() {
		return agentContribution;
	}

	public void setAgentContribution(int agentContribution) {
		this.agentContribution = agentContribution;
	}

}
