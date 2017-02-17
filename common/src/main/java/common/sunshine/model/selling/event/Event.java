package common.sunshine.model.selling.event;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.event.support.EventType;

import java.sql.Timestamp;

/**
 * 活动默认从当前时间开始,默认持续时间为7天
 * Created by sunshine on 8/23/16.
 */
public class Event extends Entity {
    /* 活动默认持续时间 */
    private final int DURATION = 7;

    private String eventId;

    /* 活动标题 */
    private String title;

    /* 活动别名, 为活动访问URL中的路径 */
    private String nickname;

    /* 活动开始时间 */
    private Timestamp start;

    /* 活动结束时间 */
    private Timestamp end;

    /* 活动类型 */
    private EventType type;


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

    public Event(String title, String nickname, Timestamp start, Timestamp end, EventType type) {
        this(title, start, end);
        this.nickname = nickname;
        this.type = type;
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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}
