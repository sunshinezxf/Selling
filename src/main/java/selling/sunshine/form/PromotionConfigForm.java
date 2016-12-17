package selling.sunshine.form;

import javax.validation.constraints.NotNull;

public class PromotionConfigForm {
	
	@NotNull
    private String buyGoods;

	@NotNull
    private String giveGoods;

	@NotNull
    private int full;

	@NotNull
    private int give;

	@NotNull
    private int criterion;

	public String getBuyGoods() {
		return buyGoods;
	}

	public void setBuyGoods(String buyGoods) {
		this.buyGoods = buyGoods;
	}

	public String getGiveGoods() {
		return giveGoods;
	}

	public void setGiveGoods(String giveGoods) {
		this.giveGoods = giveGoods;
	}

	public int getFull() {
		return full;
	}

	public void setFull(int full) {
		this.full = full;
	}

	public int getGive() {
		return give;
	}

	public void setGive(int give) {
		this.give = give;
	}

	public int getCriterion() {
		return criterion;
	}

	public void setCriterion(int criterion) {
		this.criterion = criterion;
	}
    
    

}
