package selling.sunshine.model.cashback;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.agent.lite.Agent;
import selling.sunshine.model.OrderPool;
import selling.sunshine.model.cashback.support.CashBackLevel;

/**
 * Created by sunshine on 8/10/16.
 */
public class CashBackRecord extends Entity {
    private String recordId;
    private String title;
    private double percent;
    private double amount;
    private OrderPool orderPool;
    private String description;
    private Agent agent;
    private CashBackLevel level;

    public CashBackRecord() {
        super();
        this.level = CashBackLevel.SELF;
    }

    public CashBackRecord(String title, double percent, double amount, OrderPool pool, String description, Agent agent) {
        this();
        this.title = title;
        this.percent = percent;
        this.amount = amount;
        this.orderPool = pool;
        this.description = description;
        this.agent = agent;
    }

    public CashBackRecord(String title, double percent, double amount, OrderPool pool, String description, Agent agent, CashBackLevel level) {
        this(title, percent, amount, pool, description, agent);
        this.level = level;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OrderPool getOrderPool() {
        return orderPool;
    }

    public void setOrderPool(OrderPool orderPool) {
        this.orderPool = orderPool;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public CashBackLevel getLevel() {
        return level;
    }

    public void setLevel(CashBackLevel level) {
        this.level = level;
    }
}
