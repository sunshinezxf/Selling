package selling.sunshine.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by sunshine on 5/27/16.
 * @author sunshine
 */
public class Configuration {
    private String appId;
    private String jsapiTicket;
    private String nonceStr;
    private String timestamp;
    private String signature;
    private String url;
    private String shareLink;

    public Configuration() {
        super();
        this.appId = PlatformConfig.getValue("wechat_appid");
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getJsapiTicket() {
        return jsapiTicket;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
