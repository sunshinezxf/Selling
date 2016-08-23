package common.sunshine.model.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by sunshine on 7/11/16.
 */
public class Article {

    @XStreamAlias("Title")
    private String Title;

    @XStreamAlias("Description")
    private String Description;

    @XStreamAlias("PicUrl")
    private String PicUrl;

    @XStreamAlias("Url")
    private String Url;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
