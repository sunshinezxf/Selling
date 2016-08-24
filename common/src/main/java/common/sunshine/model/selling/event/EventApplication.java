package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;

/**
 * Created by sunshine on 8/24/16.
 */
public class EventApplication extends Entity {
    private String applicationId;
    private Event event;
    private String donorName;
    private String donorPhone;
    private String doneeName;
    private String doneePhone;
    private String doneeGender;
    private String doneeAddress;
    private String relation;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorPhone() {
        return donorPhone;
    }

    public void setDonorPhone(String donorPhone) {
        this.donorPhone = donorPhone;
    }

    public String getDoneeName() {
        return doneeName;
    }

    public void setDoneeName(String doneeName) {
        this.doneeName = doneeName;
    }

    public String getDoneePhone() {
        return doneePhone;
    }

    public void setDoneePhone(String doneePhone) {
        this.doneePhone = doneePhone;
    }

    public String getDoneeGender() {
        return doneeGender;
    }

    public void setDoneeGender(String doneeGender) {
        this.doneeGender = doneeGender;
    }

    public String getDoneeAddress() {
        return doneeAddress;
    }

    public void setDoneeAddress(String doneeAddress) {
        this.doneeAddress = doneeAddress;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
