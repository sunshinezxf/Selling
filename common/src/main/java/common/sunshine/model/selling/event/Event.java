package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;

import java.util.Calendar;

/**
 * 活动默认从当前时间开始,默认持续时间为7天
 * Created by sunshine on 8/23/16.
 */
public abstract class Event extends Entity {
    private final int DURATION = 7;

    private String eventId;

    private String title;

    private String nickname;

    private Calendar start;

    private Calendar end;

    public Event() {
        this.start = Calendar.getInstance();
        this.end = Calendar.getInstance();
        this.end.add(Calendar.DATE, DURATION);
    }

    public Event(Calendar start, Calendar end) {
        this.start = start;
        this.end = end;
    }

    public Event(String title, Calendar start, Calendar end) {
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

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }
}
