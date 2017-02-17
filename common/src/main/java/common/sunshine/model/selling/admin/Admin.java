package common.sunshine.model.selling.admin;

import common.sunshine.model.Entity;

/**
 * 该类为管理员模型类
 * Created by sunshine on 4/15/16.
 */
public class Admin extends Entity {
    private String adminId;

    /* 用户名 */
    private String username;

    /* 密码 */
    private String password;

    public Admin() {
        super();
    }
    
    public Admin(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
