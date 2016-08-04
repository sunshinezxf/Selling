package selling.sunshine.form.gift;

import javax.validation.constraints.NotNull;

/**
 * Created by sunshine on 8/4/16.
 */
public class ApplyForm {
    @NotNull
    private String goodsId;

    @NotNull
    private int lastQuantity;

    @NotNull
    private int totalQuantity;

    @NotNull
    private int potential;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getLastQuantity() {
        return lastQuantity;
    }

    public void setLastQuantity(int lastQuantity) {
        this.lastQuantity = lastQuantity;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getPotential() {
        return potential;
    }

    public void setPotential(int potential) {
        this.potential = potential;
    }
}
