package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.event.support.ApplicationStatus;

import java.util.List;

/**
 * 该类为赠送活动申请
 * Created by sunshine on 8/24/16.
 */
public class EventApplication extends Entity {
    private String applicationId;

    /* 赠送申请所对应的活动 */
    private Event event;

    /* 赠送申请人姓名 */
    private String donorName;

    /* 赠送申请人电话 */
    private String donorPhone;

    /* 赠送申请人微信号 */
    private String donorWechat;

    /* 受赠人姓名 */
    private String doneeName;

    /* 受赠人电话 */
    private String doneePhone;

    /* 受赠人性别 */
    private String doneeGender;

    /* 受赠人地址 */
    private String doneeAddress;

    /* 受赠人年龄段 */
    private String doneeAgeRange;

    /* 申请当前状态 */
    private ApplicationStatus status;

    /* 赠送人与受赠人之间的关系 */
    private String relation;

    /* 祝福语, 用于制作卡片 */
    private String wishes;

    /* 表单问题回答 */
    private List<QuestionAnswer> answers;

    public EventApplication() {
        super();
    }

    public EventApplication(Event event, String donorName, String donorPhone, String doneeName, String doneePhone, String doneeGender, String doneeAddress, String doneeAgeRange, String relation, String wishes, String donorWechat) {
        this();
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

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }


}
