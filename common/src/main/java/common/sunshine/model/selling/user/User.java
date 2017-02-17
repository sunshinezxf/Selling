package common.sunshine.model.selling.user;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.admin.Admin;
import common.sunshine.model.selling.agent.lite.Agent;

/**
 * 该类为系统用户类
 * Created by sunshine on 4/23/16.
 */
public class User extends Entity {
    private String userId;

    /* 用户名 */
    private String username;

    /* 密码 */
    private String password;

    /* 角色 */
    private Role role;

    /* 关联的管理员账号, 二选一 */
    private Admin admin;

    /* 关联的代理商账号, 二选一 */
    private Agent agent;

    public User() {
        super();
    }

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
