package promotion.sunshine.form;

import javax.validation.constraints.NotNull;

/**
 * Created by wxd on 2017/2/8.
 */
public class GraphicMessageForm {

    @NotNull
    private String[] keywordList;//关键词列表

    @NotNull
    private String title;//图文标题

    @NotNull
    private String description;//图文描述

    @NotNull
    private String url;//图文url

    @NotNull
    private String picUrl;//图片url

    public String[] getKeywordList() {
        return keywordList;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public void setKeywordList(String[] keywordList) {
        this.keywordList = keywordList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
