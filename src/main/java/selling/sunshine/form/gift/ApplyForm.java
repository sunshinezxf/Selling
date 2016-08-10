package selling.sunshine.form.gift;

import javax.validation.constraints.NotNull;

/**
 * Created by sunshine on 8/4/16.
 */
public class ApplyForm {
    @NotNull
    private String goodsId;

    @NotNull
    private String lastQuantity;

    @NotNull
    private String totalQuantity;

    @NotNull
    private String potential;

    @NotNull
    private String applyLine;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getLastQuantity() {
        return lastQuantity;
    }

    public void setLastQuantity(String lastQuantity) {
        this.lastQuantity = lastQuantity;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getPotential() {
        return potential;
    }

    public void setPotential(String potential) {
        this.potential = potential;
    }

    public String getApplyLine() {
        return applyLine;
    }

    public void setApplyLine(String applyLine) {
        this.applyLine = applyLine;
    }
}
