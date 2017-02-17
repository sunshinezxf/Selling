package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;

/**
 * 该类为问题选择项
 * Created by sunshine on 8/24/16.
 */
public class QuestionOption extends Entity {
    private String optionId;

    /* 问题选择项所对应的问题 */
    private EventQuestion question;

    /* 该选择项的描述 */
    private String value;

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public EventQuestion getQuestion() {
        return question;
    }

    public void setQuestion(EventQuestion question) {
        this.question = question;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
