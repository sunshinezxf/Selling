package common.sunshine.model.selling.user;

import common.sunshine.model.Entity;

/**
 * 用户角色
 * Created by sunshine on 4/23/16.
 */
public class Role extends Entity {
    private String roleId;

    /* 角色名称, 用于鉴权 */
    private String name;

    /* 角色中文描述 */
    private String description;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
