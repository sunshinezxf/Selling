package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;

import java.util.Calendar;

/**
 * 赠送活动
 * 活动默认从当前时间开始,默认持续时间为7天
 * Created by sunshine on 8/23/16.
 */
public class Event extends Entity {
    private final int DURATION = 7;

    private String eventId;

    private String title;

    private Calendar start;

    private Calendar end;

    public Event() {
        this.start = Calendar.getInstance();
        this.end = Calendar.getInstance();
        this.end.add(Calendar.DATE, DURATION);
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
