package selling.sunshine.model;


import java.sql.Timestamp;

public class Express extends Entity{
	
	private String expressId;
	private String expressNumber;//快递单号
	private String senderName;
	private String senderPhone;
	private String senderAddress;
	private String receiverName;
	private String receiverPhone;
	private String receiverAddress;
	private String goodsName;//快递物品名称
	private OrderItem orderItem;
	
	public Express() {
		super();
	}

	public Express(String expressNumber, String senderName,
			String senderPhone, String senderAddress, String receiverName,
			String receiverPhone, String receiverAddress, String goodsName,Timestamp createAt) {
		super();
		this.expressNumber = expressNumber;
		this.senderName = senderName;
		this.senderPhone = senderPhone;
		this.senderAddress = senderAddress;
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.receiverAddress = receiverAddress;
		this.goodsName = goodsName;
		this.setCreateAt(createAt);
	}

	public String getExpressId() {
		return expressId;
	}

	public void setExpressId(String expressId) {
		this.expressId = expressId;
	}

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderPhone() {
		return senderPhone;
	}

	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}
	
	
	
	

}
