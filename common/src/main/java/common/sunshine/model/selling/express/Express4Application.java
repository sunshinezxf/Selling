package common.sunshine.model.selling.express;

import java.io.Serializable;

import common.sunshine.model.selling.order.EventOrder;

public class Express4Application extends Express implements Serializable {

	private EventOrder eventOrder;

	public Express4Application() {
		super();
	}

	public Express4Application(String expressNumber) {
		super(expressNumber);
	}

	public Express4Application(String senderName, String senderPhone, String senderAddress, String receiverName,
			String receiverPhone, String receiverAddress) {
		super(senderName, senderPhone, senderAddress, receiverName, receiverPhone, receiverAddress);
	}

	public Express4Application(String senderName, String senderPhone, String senderAddress, String receiverName,
			String receiverPhone, String receiverAddress, String goodsName) {
		super(senderName, senderPhone, senderAddress, receiverName, receiverPhone, receiverAddress, goodsName);
	}

	public Express4Application(String expressNumber, String senderName, String senderPhone, String senderAddress,
			String receiverName, String receiverPhone, String receiverAddress, String goodsName) {
		super(expressNumber, senderName, senderPhone, senderAddress, receiverName, receiverPhone, receiverAddress,
				goodsName);
	}

	public EventOrder getEventOrder() {
		return eventOrder;
	}

	public void setEventOrder(EventOrder eventOrder) {
		this.eventOrder = eventOrder;
	}

}
