package common.sunshine.model.selling.notice;

import common.sunshine.model.Entity;

/**
 * Created by sunshine on 2016/12/17.
 */
public class Notice extends Entity {
    private String noticeId;

    private String content;

    private String link;

    public Notice() {
        super();
    }

    public Notice(String content, String link) {
        this();
        this.content = content;
        this.link = link;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
