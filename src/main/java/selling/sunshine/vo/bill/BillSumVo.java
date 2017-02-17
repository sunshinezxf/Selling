package selling.sunshine.vo.bill;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.bill.support.BillStatus;

/**
 * 账单统计vo类
 * @author
 */
public class BillSumVo extends Entity{
	
	private String billId; //账单ID
	private String accountId;//关联的账户ID
	private String orderId; //关联的订单号
	private String channel;//渠道（三种：微信、支付宝、余额）
	private String clientIp;//客户端IP地址
	private String billAmount;//账单金额
	private BillStatus status;//账单状态
	
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
