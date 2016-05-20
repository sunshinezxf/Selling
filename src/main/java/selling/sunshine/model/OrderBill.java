package selling.sunshine.model;

public class OrderBill extends Bill {
	private Agent agent;
	private Order order;
	
	public OrderBill(){
		super();
		this.agent = new Agent();
		this.order = new Order();
	}
	
	public OrderBill(double billAmount, String channel, String clientIp, Agent agent, Order order) {
	    super(billAmount, channel, clientIp);
        this.agent = agent;
        this.order = order;
	}
	
	public OrderBill(double billAmount, String channel, String clientIp, BillStatus status, Agent agent, Order order) {
		super(billAmount, channel, clientIp, status);
		this.agent = agent;
		this.order = order;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	
}
