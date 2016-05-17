package selling.sunshine.form;

import javax.validation.constraints.NotNull;

/**
 * Created by sunshine on 5/17/16.
 */
public class RefundConfigForm {
    @NotNull
    private String goodsId;
    @NotNull
    private String amountTrigger;
    @NotNull
    private String percent;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getAmountTrigger() {
        return amountTrigger;
    }

    public void setAmountTrigger(String amountTrigger) {
        this.amountTrigger = amountTrigger;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
