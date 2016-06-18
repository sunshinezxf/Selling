package selling.sunshine.form;

import javax.validation.constraints.NotNull;

public class WithdrawForm {
    @NotNull
    private double money;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }


}
