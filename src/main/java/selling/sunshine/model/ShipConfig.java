package selling.sunshine.model;

import common.sunshine.model.Entity;

/**
 * Created by sunshine on 5/13/16.
 */
public class ShipConfig extends Entity {
    private String shipConfigId;
    private int date;

    public ShipConfig() {
        super();
    }

    public ShipConfig(int date) {
        this();
        this.date = date;
    }

    public String getShipConfigId() {
        return shipConfigId;
    }

    public void setShipConfigId(String shipConfigId) {
        this.shipConfigId = shipConfigId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
