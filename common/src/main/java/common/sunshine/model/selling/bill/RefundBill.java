package common.sunshine.model.selling.bill;

import common.sunshine.model.Entity;

public class RefundBill extends Entity {

	private String refundBillId;
	private String billId;
	private double billAmount;
	private double refundAmount;

	public RefundBill() {
		super();
	}

	public RefundBill(String refundBillId, String billId, double billAmount, double refundAmount) {
		super();
		this.refundBillId = refundBillId;
		this.billId = billId;
		this.billAmount = billAmount;
		this.refundAmount = refundAmount;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(double billAmount) {
		this.billAmount = billAmount;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getRefundBillId() {
		return refundBillId;
	}

	public void setRefundBillId(String refundBillId) {
		this.refundBillId = refundBillId;
	}

}
