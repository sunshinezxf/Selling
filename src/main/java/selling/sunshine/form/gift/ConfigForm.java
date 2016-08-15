package selling.sunshine.form.gift;

import javax.validation.constraints.NotNull;

/**
 * Created by sunshine on 8/15/16.
 */
public class ConfigForm {
    @NotNull
    private String agentId;

    @NotNull
    private String goodsId;

    @NotNull
    private String amount;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
