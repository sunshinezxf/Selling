package promotion.sunshine.model;

import common.sunshine.model.Entity;

/**
 * Created by sunshine on 2017/2/7.
 */
public class Article extends Entity {
    private String articleId;//订阅号文章id

    private String title;//文章标题

    private String description;//文章描述

    private String url;//文章url

    private String picUrl;//图片url

    public Article() {
        super();
    }

    public Article(String title, String description, String url, String picUrl) {
        this();
        this.title = title;
        this.description = description;
        this.url = url;
        this.picUrl = picUrl;
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
        this.picUrl = picUrl;
    }

    @Override
    public boolean equals(Object obj) {
        // 如果为同一对象的不同引用,则相同
        if (this == obj) {
            return true;
        }
        // 如果传入的对象为空,则返回false
        if (obj == null) {
            return false;
        }

        // 如果两者属于不同的类型,不能相等
        if (getClass() != obj.getClass()) {
            return false;
        }

        Article that = (Article) obj;
        return articleId.equals(that.articleId) ? true : false;
    }
}
