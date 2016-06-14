package selling.sunshine.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class PurchaseForm {
    @NotNull
	private String goodsId;
	
	private String agentId;
	
	@NotNull
	private String goodsNum;
	
	@NotNull
	private String agentName;
	
	@NotNull
	@Length(min = 11, max = 11)
	private String phone;
	
	@NotNull
	private String address;
	
	private String wechat;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
}
