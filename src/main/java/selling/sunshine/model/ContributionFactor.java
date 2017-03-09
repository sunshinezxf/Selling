package selling.sunshine.model;

import common.sunshine.model.Entity;

/**
 * 代理商贡献度配置类
 */
public class ContributionFactor extends Entity {
	
	private String factorId;
	private String factorName;
	private double factorWeight;
	
	public ContributionFactor() {
		super();
	}
	
	public String getFactorId() {
		return factorId;
	}
	public void setFactorId(String factorId) {
		this.factorId = factorId;
	}
	public String getFactorName() {
		return factorName;
	}
	public void setFactorName(String factorName) {
		this.factorName = factorName;
	}
	public double getFactorWeight() {
		return factorWeight;
	}
	public void setFactorWeight(double factorWeight) {
		this.factorWeight = factorWeight;
	}
	
	

}
