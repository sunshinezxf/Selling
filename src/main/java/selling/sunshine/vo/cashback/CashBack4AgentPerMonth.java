package selling.sunshine.vo.cashback;

/**
 * 代理商当月的返现信息
 * Created by sunshine on 8/10/16.
 */
public class CashBack4AgentPerMonth extends CashBack4Agent {
    private String month;//返现金额
    private int level;//级别（自身，上级代理商，上上级代理商）

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
    
    
}
