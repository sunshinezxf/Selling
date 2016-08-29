package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;

import java.util.List;

/**
 * Created by sunshine on 8/24/16.
 */
public class EventApplication extends Entity {
    private String applicationId;
    private Event event;
    private String donorName;
    private String donorPhone;
    private String donorWechat;
    private String doneeName;
    private String doneePhone;
    private String doneeGender;
    private String doneeAddress;
    private String doneeAgeRange;
    private String relation;
    private String wishes;
    private List<QuestionAnswer> answers;

    public EventApplication(Event event, String donorName, String donorPhone, String doneeName, String doneePhone, String doneeGender, String doneeAddress, String doneeAgeRange, String relation, String wishes, String donorWechat) {
        this.event = event;
        this.donorName = donorName;
        this.donorPhone = donorPhone;
        this.doneeName = doneeName;
        this.doneePhone = doneePhone;
        this.doneeGender = doneeGender;
        this.doneeAddress = doneeAddress;
        this.doneeAgeRange = doneeAgeRange;
        this.relation = relation;
        this.wishes = wishes;
        this.donorWechat = donorWechat;
    }
    
    public EventApplication(){
    	super();
    }

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

    public String getDoneeAgeRange() {
        return doneeAgeRange;
    }

    public void setDoneeAgeRange(String doneeAgeRange) {
        this.doneeAgeRange = doneeAgeRange;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getWishes() {
        return wishes;
    }

    public void setWishes(String wishes) {
        this.wishes = wishes;
    }

    public List<QuestionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionAnswer> answers) {
        this.answers = answers;
    }

    public String getDonorWechat() {
        return donorWechat;
    }

    public void setDonorWechat(String donorWechat) {
        this.donorWechat = donorWechat;
    }


}
