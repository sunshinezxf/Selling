package common.sunshine.model.selling.util;

import common.sunshine.model.Entity;

/**
 * 长链接对应的短链接, 使用微信的接口进行转换
 * Created by wangmin on 7/23/16.
 */
public class ShortUrl extends Entity {
    private String urlId;

    /* 长链接 */
    private String longUrl;

    /* 短链接 */
    private String shortUrl;

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

}
