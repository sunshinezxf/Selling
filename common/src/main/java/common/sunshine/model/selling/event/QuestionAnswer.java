package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;

/**
 * Created by sunshine on 8/24/16.
 */
public class QuestionAnswer extends Entity {
    private String anwerId;
    private EventApplication application;
    private String content;
    private String option;
    private int rank;

    public String getAnwerId() {
        return anwerId;
    }

    public void setAnwerId(String anwerId) {
        this.anwerId = anwerId;
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
