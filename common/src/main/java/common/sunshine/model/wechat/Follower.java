package common.sunshine.model.wechat;

import common.sunshine.model.Entity;

/**
 * Created by sunshine on 5/25/16.
 */
public class Follower extends Entity {
    private String openId;
    private String nickname;
    private short gender;
    private String city;
    private String province;

    public Follower() {
        super();
        this.gender = 0;
    }

    public Follower(String openId, String nickname, short gender, String city, String province) {
        this();
        this.openId = openId;
        this.nickname = nickname;
        this.gender = gender;
        this.city = city;
        this.province = province;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public short getGender() {
        return gender;
    }

    public void setGender(short gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
