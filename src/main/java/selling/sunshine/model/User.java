package selling.sunshine.model;

import java.util.List;

/**
 * Created by sunshine on 4/23/16.
 */
public class User extends Entity {
    private String userId;
    private String username;
    private String password;
    private List<Role> roles;

    private User() {
        super();
    }

    public User(Agent agent) {
        this();
        this.username = agent.getPhone();
        this.password = agent.getPassword();
    }

    public User(Admin admin) {
        this();
        this.username = admin.getUsername();
        this.password = admin.getPassword();
    }

    public String getUserId() {
        return userId;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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
}
