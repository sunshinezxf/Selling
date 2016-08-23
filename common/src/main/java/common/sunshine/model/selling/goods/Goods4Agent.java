package common.sunshine.model.selling.goods;

/**
 * Created by sunshine on 6/16/16.
 */
public class Goods4Agent extends AbstractGoods {
    private double agentPrice;

    public Goods4Agent() {
        super();
    }

    public Goods4Agent(String name, String description) {
        this();
        this.setName(name);
        this.setDescription(description);
    }
    
    public Goods4Agent(Goods4Customer goods4Customer){
    	this(goods4Customer.getName(), goods4Customer.getDescription());
    	this.agentPrice = goods4Customer.getAgentPrice();
    }

    public Goods4Agent(String name, double agentPrice, String description) {
        this(name, description);
        this.agentPrice = agentPrice;
    }

    public double getAgentPrice() {
        return agentPrice;
    }

    public void setAgentPrice(double agentPrice) {
        this.agentPrice = agentPrice;
    }
}
