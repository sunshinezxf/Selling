package selling.sunshine.model;

/**
 * Created by sunshine on 5/17/16.
 */
public class RefundConfig extends Entity {
	private String refundConfigId;
	private Goods goods;
	private int amountTrigger;
	private double level1Percent;
	private double level2Percent;
	private double level3Percent;

	public RefundConfig() {
		super();
	}

   

	public RefundConfig(Goods goods, int amountTrigger,
			double level1Percent, double level2Percent, double level3Percent) {
		this();
		this.goods = goods;
		this.amountTrigger = amountTrigger;
		this.level1Percent = level1Percent;
		this.level2Percent = level2Percent;
		this.level3Percent = level3Percent;
	}



	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public String getRefundConfigId() {
		return refundConfigId;
	}

	public void setRefundConfigId(String refundConfigId) {
		this.refundConfigId = refundConfigId;
	}

	public int getAmountTrigger() {
		return amountTrigger;
	}

	public void setAmountTrigger(int amountTrigger) {
		this.amountTrigger = amountTrigger;
	}

	public double getLevel1Percent() {
		return level1Percent;
	}

	public void setLevel1Percent(double level1Percent) {
		this.level1Percent = level1Percent;
	}

	public double getLevel2Percent() {
		return level2Percent;
	}

	public void setLevel2Percent(double level2Percent) {
		this.level2Percent = level2Percent;
	}

	public double getLevel3Percent() {
		return level3Percent;
	}

	public void setLevel3Percent(double level3Percent) {
		this.level3Percent = level3Percent;
	}
	
	

}
