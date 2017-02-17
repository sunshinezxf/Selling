package common.sunshine.model.selling.admin.lite;

/**
 * 该类为精简版的管理员模型, 隐藏了管理员的密码信息, 模型关联查询时使用此类
 * Created by sunshine on 5/20/16.
 */
public class Admin {
    private String id;

    /*用户名*/
    private String username;

    public Admin() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
