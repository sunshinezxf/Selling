package common.sunshine.model.selling.event;

import java.util.List;

/**
 * 赠送活动, 继承活动的抽象类
 * Created by sunshine on 8/24/16.
 */
public class GiftEvent extends Event {
    /* 赠送活动包含一系列需要用户回答的问题 */
    private List<EventQuestion> questions;

    public List<EventQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<EventQuestion> questions) {
        this.questions = questions;
    }
}
