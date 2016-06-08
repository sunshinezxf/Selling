package selling.sunshine.form;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by sunshine on 5/6/16.
 */
public class AgentLoginForm {
    @NotNull
    @Length(min = 11, max = 11)
    private String phone;

    @NotNull
    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
