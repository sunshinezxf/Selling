package selling.sunshine.vo.bill;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.bill.support.BillStatus;

public class BillSumVo extends Entity{
	
	private String billId;
	private String accountId;
	private String orderId;
	private String channel;
	private String clientIp;
	private String billAmount;
	private BillStatus status;
	
	public BillSumVo() {
		super();
	}

	public BillSumVo(String billId, String accountId, String orderId, String channel, String clientIp,
			String billAmount, BillStatus status) {
		super();
		this.billId = billId;
		this.accountId = accountId;
		this.orderId = orderId;
		this.channel = channel;
		this.clientIp = clientIp;
		this.billAmount = billAmount;
		this.status = status;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}

	public BillStatus getStatus() {
		return status;
	}

	public void setStatus(BillStatus status) {
		this.status = status;
	}
	
	

}
