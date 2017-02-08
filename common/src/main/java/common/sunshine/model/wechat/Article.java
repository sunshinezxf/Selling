package common.sunshine.model.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Objects;

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

        // 类型相同, 比较内容是否相同
        Article other = (Article) obj;

        return Objects.equals(Title, other.Title) && Objects.equals(Description, other.Description) && Objects.equals(PicUrl, other.PicUrl) && Objects.equals(Url, other.Url);
    }

}
