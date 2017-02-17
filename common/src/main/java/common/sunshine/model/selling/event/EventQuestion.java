package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.event.support.ChoiceType;

import java.util.List;

/**
 * 该类为赠送活动的问题类
 * Created by sunshine on 8/24/16.
 */
public class EventQuestion extends Entity {
    private String questionId;

    /* 问题所属活动 */
    private Event event;

    /* 问题的内容 */
    private String content;

    /* 问题的序号 */
    private int rank;

    /* 问题的类型 */
    private ChoiceType type;

    /* 问题的所有选项列表 */
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
