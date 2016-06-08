package selling.sunshine.model;

/**
 * Created by sunshine on 4/23/16.
 */
public class User extends Entity {
    private String userId;
    private String username;
    private String password;
    private Role role;
    private Admin admin;
    private selling.sunshine.model.lite.Agent agent;

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

    public selling.sunshine.model.lite.Agent getAgent() {
        return agent;
    }

    public void setAgent(selling.sunshine.model.lite.Agent agent) {
        this.agent = agent;
    }
}
