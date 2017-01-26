package common.sunshine.model.selling.goods;

import common.sunshine.model.selling.goods.support.GoodsType;

/**
 * Created by sunshine on 6/16/16.
 */
public class Goods4Agent extends AbstractGoods {
    private double agentPrice;

    public Goods4Agent() {
        super();
        this.setType(GoodsType.REAL);
    }

    public Goods4Agent(String name, String description, String standard) {
        this();
        this.setName(name);
        this.setDescription(description);
        this.setStandard(standard);
    }

    public Goods4Agent(Goods4Customer goods4Customer) {
        this(goods4Customer.getName(), goods4Customer.getDescription(), goods4Customer.getStandard());
        this.agentPrice = goods4Customer.getAgentPrice();
    }

    public Goods4Agent(String name, double agentPrice, String description, String standard) {
        this(name, description, standard);
        this.agentPrice = agentPrice;
    }

    public Goods4Agent(String name, double agentPrice, String description, String standard, String measure, String produceNo, String produceDate) {
        this(name, agentPrice, description, standard);
        this.setMeasure(measure);
        this.setProduceNo(produceNo);
        this.setProduceDate(produceDate);
    }

    public double getAgentPrice() {
        return agentPrice;
    }

    public void setAgentPrice(double agentPrice) {
        this.agentPrice = agentPrice;
    }

    @Override
    public int hashCode() {
        return this.getGoodsId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof Goods4Agent) {
            return this.getGoodsId().equals(((Goods4Agent) obj).getGoodsId());
        }
        return false;
    }


}
