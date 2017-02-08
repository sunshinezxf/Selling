package promotion.sunshine.model;

import common.sunshine.model.Entity;

/**
 * Created by sunshine on 2017/2/7.
 */
public class Article extends Entity {
    private String articleId;

    private String title;

    private String description;

    private String url;

    private String picUrl;

    public Article() {
        super();
    }

    public Article(String title, String description, String url,String picUrl) {
        this();
        this.title = title;
        this.description = description;
        this.url = url;
        this.picUrl=picUrl;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        picUrl = picUrl;
    }
}
