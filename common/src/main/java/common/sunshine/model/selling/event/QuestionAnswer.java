package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;

/**
 * 该类为赠送活动的问题的答案
 * Created by sunshine on 8/24/16.
 */
public class QuestionAnswer extends Entity {
    private String answerId;

    /* 所对应的用户的申请 */
    private EventApplication application;

    /* 申请的内容 */
    private String content;
    private String option;
    private int rank;

    public QuestionAnswer() {
        super();
    }

    public QuestionAnswer(EventApplication application, String content, String option, int rank) {
        this.application = application;
        this.content = content;
        this.option = option;
        this.rank = rank;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public EventApplication getApplication() {
        return application;
    }

    public void setApplication(EventApplication application) {
        this.application = application;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
