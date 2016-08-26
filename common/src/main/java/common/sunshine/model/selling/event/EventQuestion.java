package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.event.support.ChoiceType;

import java.util.List;

/**
 * Created by sunshine on 8/24/16.
 */
public class EventQuestion extends Entity {
    private String questionId;
    private Event event;
    private String content;
    private int rank;
    private ChoiceType type;
    private List<QuestionOption> options;

    public EventQuestion() {
        this.type = ChoiceType.EXCLUSIVE;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public ChoiceType getType() {
        return type;
    }

    public void setType(ChoiceType type) {
        this.type = type;
    }

    public List<QuestionOption> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionOption> options) {
        this.options = options;
    }

}
