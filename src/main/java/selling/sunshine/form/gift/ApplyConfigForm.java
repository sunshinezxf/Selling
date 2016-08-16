package selling.sunshine.form.gift;

import javax.validation.constraints.NotNull;

/**
 * Created by sunshine on 8/15/16.
 */
public class ApplyConfigForm {
    @NotNull
    private String applyId;

    @NotNull
    private String amount;

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
