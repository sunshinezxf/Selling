package selling.sunshine.form;

import javax.validation.constraints.NotNull;

/**
 * Created by sunshine on 5/31/16.
 */
public class AgentLetheForm {
    @NotNull
    private String name;
    @NotNull
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
