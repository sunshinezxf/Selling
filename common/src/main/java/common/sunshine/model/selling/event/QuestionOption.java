package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;

/**
 * Created by sunshine on 8/24/16.
 */
public class QuestionOption extends Entity {
    private String optionId;
    private EventQuestion question;
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
