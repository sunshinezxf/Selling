package selling.sunshine.model;

import common.sunshine.model.Entity;
import common.sunshine.model.selling.goods.Goods4Agent;
import common.sunshine.model.selling.goods.Goods4Customer;

/**
 * Created by sunshine on 5/17/16.
 * 返现配置
 */
public class RefundConfig extends Entity {
    private String refundConfigId;//配置ID
    private int amountTrigger;//购买数量下限
    private int amountTopTrigger;//购买数量上限，默认值为0，代表购买数量在amountTrigger以上
    private double level1Percent;//一级返现百分比
    private double level2Percent;//二级返现百分比
    private double level3Percent;//三级返现百分比
    private int monthConfig;//月份配置
    private Goods4Agent goods;//商品
    private boolean universal;//是否普遍适用
    private int universalMonth;//假如不是普遍适用的话，代表的是前n个月的返现配置

    public RefundConfig() {
        super();
    }

    public RefundConfig(Goods4Customer goods, int amountTrigger, double level1Percent, double level2Percent, double level3Percent, int monthConfig) {
        this();
        this.goods = goods;
        this.amountTrigger = amountTrigger;
        this.level1Percent = level1Percent;
        this.level2Percent = level2Percent;
        this.level3Percent = level3Percent;
        this.monthConfig = monthConfig;
    }

    public RefundConfig(Goods4Customer goods, int amountTrigger, double level1Percent, double level2Percent, double level3Percent) {
        this();
        this.goods = goods;
        this.amountTrigger = amountTrigger;
        this.level1Percent = level1Percent;
        this.level2Percent = level2Percent;
        this.level3Percent = level3Percent;
    }

    public RefundConfig(String refundConfigId, int amountTrigger, double level1Percent, double level2Percent,
			double level3Percent, int monthConfig, Goods4Agent goods, boolean universal, int universalMonth) {
		super();
		this.refundConfigId = refundConfigId;
		this.amountTrigger = amountTrigger;
		this.level1Percent = level1Percent;
		this.level2Percent = level2Percent;
		this.level3Percent = level3Percent;
		this.monthConfig = monthConfig;
		this.goods = goods;
		this.universal = universal;
		this.universalMonth = universalMonth;
	}

    public RefundConfig(String refundConfigId, int amountTrigger, int amountTopTrigger, double level1Percent, double level2Percent, double level3Percent, int monthConfig, Goods4Agent goods, boolean universal, int universalMonth) {
        this.refundConfigId = refundConfigId;
        this.amountTrigger = amountTrigger;
        this.amountTopTrigger = amountTopTrigger;
        this.level1Percent = level1Percent;
        this.level2Percent = level2Percent;
        this.level3Percent = level3Percent;
        this.monthConfig = monthConfig;
        this.goods = goods;
        this.universal = universal;
        this.universalMonth = universalMonth;
    }

    public Goods4Agent getGoods() {
        return goods;
    }

    public void setGoods(Goods4Agent goods) {
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

    public int getMonthConfig() {
        return monthConfig;
    }

    public void setMonthConfig(int monthConfig) {
        this.monthConfig = monthConfig;
    }

	public int getUniversalMonth() {
		return universalMonth;
	}

	public void setUniversalMonth(int universalMonth) {
		this.universalMonth = universalMonth;
	}

	public boolean isUniversal() {
		return universal;
	}

	public void setUniversal(boolean universal) {
		this.universal = universal;
	}

    public int getAmountTopTrigger() {
        return amountTopTrigger;
    }

    public void setAmountTopTrigger(int amountTopTrigger) {
        this.amountTopTrigger = amountTopTrigger;
    }
}
