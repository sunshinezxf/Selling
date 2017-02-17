package common.sunshine.model.selling.notice;

import common.sunshine.model.Entity;

/**
 * 系统通知类, 会出现在移动端顾客商城中
 * Created by sunshine on 2016/12/17.
 */
public class Notice extends Entity {
    private String noticeId;

    /* 通知内容 */
    private String content;

    /* 通知外链 */
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
