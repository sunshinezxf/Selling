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

	private String amountTriggerTop;
    @NotNull
    private String level1Percent;
    @NotNull
    private String level2Percent;
    @NotNull
    private String level3Percent;
    @NotNull
    private String monthConfig;
    @NotNull
    private String applyMonths;


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

	public String getLevel1Percent() {
		return level1Percent;
	}

	public void setLevel1Percent(String level1Percent) {
		this.level1Percent = level1Percent;
	}

	public String getLevel2Percent() {
		return level2Percent;
	}

	public void setLevel2Percent(String level2Percent) {
		this.level2Percent = level2Percent;
	}

	public String getLevel3Percent() {
		return level3Percent;
	}

	public void setLevel3Percent(String level3Percent) {
		this.level3Percent = level3Percent;
	}

	public String getMonthConfig() {
		return monthConfig;
	}

	public void setMonthConfig(String monthConfig) {
		this.monthConfig = monthConfig;
	}

	public String getApplyMonths() {
		return applyMonths;
	}

	public void setApplyMonths(String applyMonths) {
		this.applyMonths = applyMonths;
	}

	public String getAmountTriggerTop() {
		return amountTriggerTop;
	}

	public void setAmountTriggerTop(String amountTriggerTop) {
		this.amountTriggerTop = amountTriggerTop;
	}
}
