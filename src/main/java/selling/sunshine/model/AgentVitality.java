package selling.sunshine.model;

import common.sunshine.model.Entity;

/*
 * 代理商活跃度审核表
 */
public class AgentVitality extends Entity {
	
	private String agentVitalityId;//活跃度ID
	private int vitalityQuantity;//数量
	private double vitalityPrice;//金额
	
	public AgentVitality() {
		 super();
	}

	public AgentVitality(String agentVitalityId, int vitalityQuantity, double vitalityPrice) {
		super();
		this.agentVitalityId = agentVitalityId;
		this.vitalityQuantity = vitalityQuantity;
		this.vitalityPrice = vitalityPrice;
	}

	public String getAgentVitalityId() {
		return agentVitalityId;
	}

	public void setAgentVitalityId(String agentVitalityId) {
		this.agentVitalityId = agentVitalityId;
	}

	public int getVitalityQuantity() {
		return vitalityQuantity;
	}

	public void setVitalityQuantity(int vitalityQuantity) {
		this.vitalityQuantity = vitalityQuantity;
	}

	public double getVitalityPrice() {
		return vitalityPrice;
	}

	public void setVitalityPrice(double vitalityPrice) {
		this.vitalityPrice = vitalityPrice;
	}
	
	
	
}
