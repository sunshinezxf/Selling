package selling.sunshine.model;

public class RefundRecord extends Entity {

	private String refundRecordId;
	private String refundName;
	private double refundPercent;
	private double refundAmount;
	private OrderPool orderPool;

	public RefundRecord() {
		super();
	}

	public RefundRecord(String refundRecordId, String refundName,
			double refundPercent, int refundAmount, OrderPool orderPool) {
		this();
		this.refundRecordId = refundRecordId;
		this.refundName = refundName;
		this.refundPercent = refundPercent;
		this.refundAmount = refundAmount;
		this.orderPool = orderPool;
	}

	public String getRefundRecordId() {
		return refundRecordId;
	}

	public void setRefundRecordId(String refundRecordId) {
		this.refundRecordId = refundRecordId;
	}

	public String getRefundName() {
		return refundName;
	}

	public void setRefundName(String refundName) {
		this.refundName = refundName;
	}

	public double getRefundPercent() {
		return refundPercent;
	}

	public void setRefundPercent(double refundPercent) {
		this.refundPercent = refundPercent;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public OrderPool getOrderPool() {
		return orderPool;
	}

	public void setOrderPool(OrderPool orderPool) {
		this.orderPool = orderPool;
	}

}