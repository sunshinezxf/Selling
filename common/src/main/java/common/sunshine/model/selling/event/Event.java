package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * 活动默认从当前时间开始,默认持续时间为7天
 * Created by sunshine on 8/23/16.
 */
public abstract class Event extends Entity {
    private final int DURATION = 7;

    private String eventId;

    private String title;

    private String nickname;

    private Timestamp start;

    private Timestamp end;
    

    public Event() {
        this.start = new Timestamp(System.currentTimeMillis());
        this.end = new Timestamp(start.getTime() + 24 * 60 * 60 * 1000 * DURATION);
    }

    public Event(Timestamp start, Timestamp end) {
        this.start = start;
        this.end = end;
    }

    public Event(String title, Timestamp start, Timestamp end) {
        this(start, end);
        this.title = title;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }
}
