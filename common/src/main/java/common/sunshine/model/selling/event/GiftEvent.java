package common.sunshine.model.selling.event;

import java.util.List;

/**
 * Created by sunshine on 8/24/16.
 */
public class GiftEvent extends Event {
    private List<EventQuestion> questions;

    public List<EventQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<EventQuestion> questions) {
        this.questions = questions;
    }
}
