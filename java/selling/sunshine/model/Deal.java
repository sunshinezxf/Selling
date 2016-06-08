package selling.sunshine.model;

/**
 * Created by sunshine on 5/10/16.
 */
public class Deal extends Entity {
    private String dealId;

    public Deal() {
        super();
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }
}
